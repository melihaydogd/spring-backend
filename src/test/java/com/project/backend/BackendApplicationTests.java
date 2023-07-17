package com.project.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.backend.dto.auth.AuthenticationRequest;
import com.project.backend.dto.auth.RefreshTokenRequest;
import com.project.backend.dto.auth.RegisterRequest;
import com.project.backend.model.token.Token;
import com.project.backend.model.token.TokenRepository;
import org.json.JSONObject;
import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BackendApplicationTests {

	@ClassRule
	public static PostgresContainer postgres = PostgresContainer.getInstance();

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TokenRepository tokenRepository;

	private String firstLoginAccessToken;
	private String firstLoginRefreshToken;
	private String secondLoginAccessToken;

	@BeforeAll
	public void setup() {
		postgres.runInitScript();
	}

	@Test
	@Order(1)
	public void login() throws Exception {
		ResultActions result = this.mockMvc
				.perform(
						post("/auth/login")
								.content(objectMapper.writeValueAsString(new AuthenticationRequest("amelih6@gmail.com", "pass")))
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accessToken").exists())
				.andExpect(jsonPath("$.refreshToken").exists());

		JSONObject response = new JSONObject(result.andReturn().getResponse().getContentAsString());
		firstLoginAccessToken = response.getString("accessToken");
		firstLoginRefreshToken = response.getString("refreshToken");
	}

	@Test
	@Order(2)
	public void demo() throws Exception {
		this.mockMvc
				.perform(
						get("/demo")
								.header("Authorization", "Bearer " + firstLoginAccessToken)
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Hello world")));
	}

	@Test
	@Order(3)
	public void refreshToken() throws Exception {
		Thread.sleep(1000); // To make sure, newly created JWT is unique. Otherwise, their issued at times are the same and same JWT is generated.
		this.mockMvc
				.perform(
						post("/auth/refreshToken")
								.content(objectMapper.writeValueAsString(new RefreshTokenRequest(firstLoginRefreshToken)))
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accessToken").exists())
				.andExpect(jsonPath("$.refreshToken").exists());
	}

	@Test
	@Order(4)
	public void logout() throws Exception {
		this.mockMvc
				.perform(
						get("/auth/logout")
								.header("Authorization", "Bearer " + firstLoginAccessToken)
				)
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@Order(5)
	public void checkRevoke() throws Exception {
		Optional<Token> token = tokenRepository.findByToken(firstLoginAccessToken);
		assert token.isPresent();
		assert Objects.equals(token.get().isRevoked(), true);
	}

	@Test
	@Order(6)
	public void register() throws Exception {
		ResultActions result = this.mockMvc
				.perform(
						post("/auth/register")
								.content(objectMapper.writeValueAsString(new RegisterRequest("test", "test","test@gmail.com", "pass")))
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accessToken").exists())
				.andExpect(jsonPath("$.refreshToken").exists());

		JSONObject response = new JSONObject(result.andReturn().getResponse().getContentAsString());
		secondLoginAccessToken = response.getString("accessToken");
	}

	@Test
	@Order(7)
	public void demo2() throws Exception {
		this.mockMvc
				.perform(
						get("/demo")
								.header("Authorization", "Bearer " + secondLoginAccessToken)
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Hello world")));
	}

}
