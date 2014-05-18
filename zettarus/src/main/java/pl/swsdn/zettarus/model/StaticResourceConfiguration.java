package pl.swsdn.zettarus.model;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {

	public static final String MEDIA_PREFIX = "/media/";
	
	@Value("#{'${rootFolder.path}'}")
	private File rootFolder;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(MEDIA_PREFIX + "**").addResourceLocations(
				rootFolder.toURI().toString());
	}
}
