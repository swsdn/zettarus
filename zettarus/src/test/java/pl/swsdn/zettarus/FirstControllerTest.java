package pl.swsdn.zettarus;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import pl.swsdn.zettarus.web.FirstController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@WebAppConfiguration
public class FirstControllerTest {

	@Autowired
	private WebApplicationContext ctx;

	@Autowired
	FirstController controller;

	MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = webAppContextSetup(ctx).build();
	}

	@Test
	public void should_retrieve_index_page() throws Exception {
		mockMvc.perform(get("/")).andDo(print())
				.andExpect(status().is2xxSuccessful());
	}
}
