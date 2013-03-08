package com.ushahidi.swiftriver.core.api.dao.impl;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ushahidi.swiftriver.core.api.dao.AbstractDaoTest;
import com.ushahidi.swiftriver.core.model.Drop;
import com.ushahidi.swiftriver.core.model.Identity;
import com.ushahidi.swiftriver.core.model.Media;
import com.ushahidi.swiftriver.core.model.Place;
import com.ushahidi.swiftriver.core.model.Tag;

public class JpaDropDaoTest extends AbstractDaoTest {

	@Autowired
	JpaDropDao dropDao;

	@Test
	public void createDrops() {
		List<Drop> drops = new ArrayList<Drop>();
		Drop drop = new Drop();
		drop.setChannel("test channel");
		drop.setTitle("test title");
		drop.setContent("test content");
		drop.setDatePublished(new Date(1362724400000L));
		drop.setOriginalId("test original id");

		Identity identity = new Identity();
		identity.setOriginId("test identity original id");
		drop.setIdentity(identity);
		drops.add(drop);
		
		List<Long> riverIds = new ArrayList<Long>();
		riverIds.add(1L);
		drop.setRiverIds(riverIds);

		Tag tag = new Tag();
		tag.setTag(" Test tag ");
		tag.setType(" Just a test ");
		List<Tag> tags = new ArrayList<Tag>();
		tags.add(tag);
		drop.setTags(tags);

		List<Place> places = new ArrayList<Place>();
		Place place = new Place();
		place.setPlaceName(" Neverland ");
		place.setLongitude(-35.2033f);
		place.setLatitude(31.9216f);
		places.add(place);
		drop.setPlaces(places);

		List<Media> media = new ArrayList<Media>();
		Media newMedia = new Media();
		newMedia.setUrl("http://example.com/new ");
		media.add(newMedia);

		dropDao.createDrops(drops);

		String sql = "SELECT `id`, `channel`, `droplet_hash`, "
				+ "`droplet_orig_id`, `droplet_title`, "
				+ "`droplet_content`, `droplet_date_pub`, "
				+ "`droplet_date_add`, `identity_id` FROM `droplets` WHERE "
				+ "`id` =  ?";

		Map<String, Object> results = this.jdbcTemplate.queryForMap(sql,
				drop.getId());

		assertEquals(11L, ((BigInteger) results.get("id")).longValue());
		assertEquals("test channel", results.get("channel"));
		assertEquals("476029d4b6f84664ac56d93e9f6fd27a",
				results.get("droplet_hash"));
		assertEquals("test title", results.get("droplet_title"));
		assertEquals("test content", results.get("droplet_content"));
		assertEquals("test original id", results.get("droplet_orig_id"));
		assertNotNull(results.get("droplet_date_pub"));
		assertNotNull(results.get("droplet_date_add"));
		assertEquals(3L, ((BigInteger) results.get("identity_id")).longValue());
		assertNotNull(tag.getId());
		assertNotNull(place.getId());
		assertNotNull(newMedia.getId());

		sql = "SELECT `river_id`, `droplet_date_pub`, `channel` FROM `rivers_droplets` WHERE `droplet_id` = ?";

		results = this.jdbcTemplate.queryForMap(sql, drop.getId());
		assertEquals(1L, ((BigInteger) results.get("river_id")).longValue());
		assertEquals(drop.getDatePublished().getTime(), ((Date)results.get("droplet_date_pub")).getTime());
		assertEquals("test channel", results.get("channel"));
	}
}