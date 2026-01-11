export default {
  async fetch(request) {
    const originalUrl = new URL(request.url);
    const targetUrl = new URL("https://szyszko-apka.vercel.app");

    targetUrl.pathname = originalUrl.pathname;
    targetUrl.search = originalUrl.search;

    const proxyRequest = new Request(targetUrl.toString(), {
        method: request.method,
        headers: request.headers,
        body: request.body,
        redirect: "follow"
    });

    return fetch(proxyRequest);
  }
}