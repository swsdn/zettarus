package pl.swsdn.zettarus.model;

import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserUUID {

	private final UUID uuid = UUID.randomUUID();

	public UUID getSessionValue() {
		return uuid;
	}

}
