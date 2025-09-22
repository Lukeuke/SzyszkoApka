package v1

import (
	"context"
	"io"
	"net/http"
	helpers "szyszko-api/application/helpers"
	repository "szyszko-api/infrastructure/repositories"

	"github.com/aws/aws-sdk-go-v2/aws"
	"github.com/aws/aws-sdk-go-v2/config"
	"github.com/aws/aws-sdk-go-v2/credentials"
	"github.com/aws/aws-sdk-go-v2/service/s3"
	"github.com/gin-gonic/gin"
)

var (
	bucketName  string
	r2Region    = "auto"
	r2Endpoint  string
	r2AccessKey string
	r2SecretKey string
)

type ImageHandler struct {
	uow *repository.UnitOfWork
}

func NewImageHandler(uow *repository.UnitOfWork) *ImageHandler {
	bucketName = helpers.MustGetenv("R2_BUCKET_NAME")
	r2Endpoint = helpers.MustGetenv("R2_ENDPOINT_URL")
	r2AccessKey = helpers.MustGetenv("R2_ACCESS_KEY")
	r2SecretKey = helpers.MustGetenv("R2_SECRET_ACCESS_KEY")

	return &ImageHandler{
		uow: uow,
	}
}

func RegisterImage(group *gin.RouterGroup, uow *repository.UnitOfWork) {
	handler := NewImageHandler(uow)
	image := group.Group("/images")

	image.GET("/:id", handler.getImage)
}

func (h *ImageHandler) getImage(c *gin.Context) {
	objectKey := c.Param("id")

	cfg, err := config.LoadDefaultConfig(context.TODO(),
		config.WithRegion(r2Region),
		config.WithCredentialsProvider(credentials.NewStaticCredentialsProvider(r2AccessKey, r2SecretKey, "")),
		config.WithEndpointResolverWithOptions(aws.EndpointResolverWithOptionsFunc(func(service, region string, options ...interface{}) (aws.Endpoint, error) {
			return aws.Endpoint{
				URL:           r2Endpoint,
				SigningRegion: r2Region,
			}, nil
		})),
	)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "config error"})
		return
	}

	client := s3.NewFromConfig(cfg)

	resp, err := client.GetObject(context.TODO(), &s3.GetObjectInput{
		Bucket: aws.String(bucketName),
		Key:    aws.String(objectKey),
	})
	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "Image not found"})
		return
	}
	defer resp.Body.Close()

	c.Header("Content-Type", *resp.ContentType)
	c.Header("Cache-Control", "public, max-age=300, stale-while-revalidate=3600")
	c.Status(http.StatusOK)

	_, err = io.Copy(c.Writer, resp.Body)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to stream image"})
		return
	}
}
