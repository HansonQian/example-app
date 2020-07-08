package example.gateway.fallback;

import com.netflix.zuul.context.RequestContext;
import example.enums.CommonCodeEnum;
import example.result.DataResult;
import example.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @className: ZuulGatewayFallback
 * @description: Zuul回退机制实现
 * @author: HanSon.Q
 * @version: V1.0
 */
@Slf4j
public class ZuulGatewayFallback implements FallbackProvider {
    /**
     * @return 返回* 表示对所有服务进行回退操作
     * <p>
     * 如果只想对某个服务进行回退,那么就需要返回需要回退的服务名称,该名称一定要在Eureka服务注册中心中存在
     */
    @Override
    public String getRoute() {
        return "*";
    }

    /**
     * 构造回退的内容
     *
     * @param route 路由
     * @param cause 异常
     * @return ClientHttpResponse
     */
    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        log.error("当前回退路由:{},异常信息:{}", route, cause);

        return new ClientHttpResponse() {
            /**
             * @return 设置响应头信息
             */
            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                MediaType mediaType = new MediaType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                headers.setContentType(mediaType);
                return headers;
            }

            /**
             * @return 设置响应体
             * @throws IOException IO异常
             */
            @Override
            public InputStream getBody() throws IOException {
                RequestContext ctx = RequestContext.getCurrentContext();
                Throwable throwable = ctx.getThrowable();
                if (null != throwable) {
                    log.error("", throwable.getCause());
                }
                DataResult data = new DataResult(CommonCodeEnum.SERVER_ERROR);
                String json = JsonUtil.serialize(data);
                byte[] bytes = json == null ? null : json.getBytes();
                if (null == bytes) {
                    return null;
                }
                return new ByteArrayInputStream(bytes);
            }

            /**
             * @return Return the HTTP status code of the response.
             * @throws IOException IO异常
             */
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            /**
             *
             * @return 设置返回的状态码
             * @throws IOException IO异常
             */
            @Override
            public int getRawStatusCode() throws IOException {
                return this.getStatusCode().value();
            }

            /**
             * @return 返回状态码对应的文本信息
             * @throws IOException IO异常
             */
            @Override
            public String getStatusText() throws IOException {
                return this.getStatusCode().getReasonPhrase();
            }

            @Override
            public void close() {
                InputStream body = null;
                try {
                    body = this.getBody();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (null != body) {
                        try {
                            body.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        };
    }
}
