/**
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.ushahidi.swiftriver.core.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;

public class RiversControllerTest extends AbstractControllerTest {

	@Test
	@Transactional
	public void createRiver() throws Exception {
		String postBody = "{\"name\":\"Viva Riva\",\"description\":\"Like the movie\",\"public\":true}";
		
		this.mockMvc
				.perform(
						post("/v1/rivers").content(postBody)
								.contentType(MediaType.APPLICATION_JSON)
								.principal(getAuthentication("user1")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(3));
	}
	
	@Test
	@Transactional
	public void createRiverWithoutQuota() throws Exception {
		String postBody = "{\"name\":\"Viva Riva\",\"description\":\"Like the movie\",\"public\":true}";
		
		this.mockMvc
				.perform(
						post("/v1/rivers").content(postBody)
								.contentType(MediaType.APPLICATION_JSON)
								.principal(getAuthentication("user2")))
				.andExpect(status().isForbidden());
	}
	
	@Test
	@Transactional
	public void createRiverWithoutDuplicateName() throws Exception {
		String postBody = "{\"name\":\"Public River 1\",\"description\":\"Like the movie\",\"public\":true}";
		
		this.mockMvc
				.perform(
						post("/v1/rivers").content(postBody)
								.contentType(MediaType.APPLICATION_JSON)
								.principal(getAuthentication("user1")))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void getRiverWithNonExistentId() throws Exception {
		this.mockMvc.perform(get("/v1/rivers/9999")).andExpect(
				status().isNotFound());
	}

	@Test
	public void getRiverById() throws Exception {
		this.mockMvc
				.perform(get("/v1/rivers/1"))
				.andExpect(status().isOk())
				.andExpect(
						content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.account.id").value(3))
				.andExpect(jsonPath("$.account.account_path").value("user1"))
				.andExpect(jsonPath("$.active").value(true))
				.andExpect(jsonPath("$.full").value(false))
				.andExpect(jsonPath("$.name").value("Public River 1"))
				.andExpect(jsonPath("$.follower_count").value(0))
				.andExpect(jsonPath("$.public").value(true))
				.andExpect(jsonPath("$.drop_count").value(100))
				.andExpect(jsonPath("$.drop_quota").value(10000))
				.andExpect(jsonPath("$.extension_count").value(0))
				.andExpect(jsonPath("$.channels").isArray());
	}

	@Test
	public void getDropsFromNonExistentRiver() throws Exception {
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				"user1", "password");
		SecurityContextHolder.getContext().setAuthentication(authentication);

		this.mockMvc.perform(
				get("/v1/rivers/9999/drops").principal(authentication))
				.andExpect(status().isNotFound());
	}

	@Test
	public void getDropsFromEmptyRiver() throws Exception {
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				"user2", "password");
		SecurityContextHolder.getContext().setAuthentication(authentication);

		this.mockMvc
				.perform(get("/v1/rivers/2/drops").principal(authentication))
				.andExpect(status().isOk()).andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").value(empty()));
	}

	@Test
	public void getDrops() throws Exception {
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				"user1", "password");
		SecurityContextHolder.getContext().setAuthentication(authentication);

		this.mockMvc
				.perform(get("/v1/rivers/1/drops").principal(authentication))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].id").exists())
				.andExpect(jsonPath("$[0].title").exists())
				.andExpect(jsonPath("$[0].content").exists())
				.andExpect(jsonPath("$[0].channel").exists())
				.andExpect(jsonPath("$[0].original_id").exists())
				.andExpect(jsonPath("$[0].original_url").exists())
				// FIXME:.andExpect(jsonPath("$[0].original_place").exists())
				.andExpect(jsonPath("$[0].source").exists())
				.andExpect(jsonPath("$[0].date_published").exists())
				// FIXME:.andExpect(jsonPath("$[0].user_score").exists())
				.andExpect(jsonPath("$[0].comment_count").exists())
				// FIXME: .andExpect(jsonPath("$[0].buckets").isArray())
				.andExpect(jsonPath("$[0].tags").isArray())
				.andExpect(jsonPath("$[0].links").isArray())
				.andExpect(jsonPath("$[0].media").isArray())
				.andExpect(jsonPath("$[0].places").isArray());
	}

	/**
	 * Test for {@link RiversController#deleteRiver(Long)}
	 * 
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void deleteRiver() throws Exception {
		this.mockMvc.perform(delete("/v1/rivers/1")).andExpect(status().isOk());
	}

	/**
	 * Test for {@link RiversController#deleteRiver(Long)} where the specified
	 * does not exist in which case a 404 should be returned
	 * 
	 * @throws Exception
	 */
	@Test
	public void deleteNonExistentRiver() throws Exception {
		this.mockMvc.perform(delete("/v1/rivers/500")).andExpect(
				status().isNotFound());
	}

	/**
	 * Test for {@link RiversController#getCollaborators(Long)}
	 * 
	 * @throws Exception
	 */
	@Test
	public void getCollaborators() throws Exception {
		this.mockMvc
				.perform(get("/v1/rivers/1/collaborators"))
				.andExpect(status().isOk())
				.andExpect(
						content().contentType("application/json;charset=UTF-8"));
	}

	/**
	 * Test for {@link RiversController#modifyCollaborator(Long, Long, Map)}
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	@Transactional
	public void modifyCollaborator() throws Exception {
		// Test data
		Object[][] collaborotorData = { { "read_only", false },
				{ "active", false } };

		Map<String, Object> collaboratorMap = ArrayUtils
				.toMap(collaborotorData);

		this.mockMvc
				.perform(
						put("/v1/rivers/1/collaborators/3")
								.accept(MediaType.APPLICATION_JSON)
								.contentType(MediaType.APPLICATION_JSON)
								.content(
										new ObjectMapper()
												.writeValueAsBytes(collaboratorMap)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.read_only").value(false))
				.andExpect(jsonPath("$.active").value(false));
	}

	/**
	 * Test for {@link RiversController#deleteCollaborator(Long, Long)}
	 * 
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void deleteCollaborator() throws Exception {
		this.mockMvc.perform(delete("/v1/rivers/1/collaborators/2")).andExpect(
				status().isOk());
	}

	/**
	 * Test for {@link RiversController#getFollowers(Long)}
	 * 
	 * @throws Exception
	 */
	@Test
	public void getFollowers() throws Exception {
		this.mockMvc.perform(get("/v1/rivers/1/followers"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*]").isArray());
	}

	/**
	 * Test for {@link RiversController#deleteFollower(Long, Long)}
	 * 
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void deleteFollower() throws Exception {
		this.mockMvc.perform(delete("/v1/rivers/1/followers/4")).andExpect(
				status().isOk());
	}
}
