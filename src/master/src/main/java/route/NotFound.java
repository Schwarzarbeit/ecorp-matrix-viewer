package route;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import types.TemplateRoute;
import types.WebRoute;
import types.WebSession;

public class NotFound extends TemplateRoute {
    /**
     * Constructor
     *
     * @param method
     * @param path
     */
    public NotFound(HttpMethod method, String path) {
        super(method, path);
    }

    /**
     * Get template path
     * @return path to the template
     */
    @Override
    public String GetTemplate() {
        return "/404.twig";
    }
}