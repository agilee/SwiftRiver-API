/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/agpl.html>
 * 
 * Copyright (C) Ushahidi Inc. All Rights Reserved.
 */
package com.ushahidi.swiftriver.core.api.dao.impl;

import org.springframework.stereotype.Repository;

import com.ushahidi.swiftriver.core.api.dao.ChannelDao;
import com.ushahidi.swiftriver.core.model.Channel;

@Repository
public class JpaChannelDao extends AbstractJpaDao implements ChannelDao {

	@Override
	public Channel update(Channel channel) {
		return em.merge(channel);
	}

	@Override
	public void delete(Channel channel) {
		em.remove(channel);

	}

	@Override
	public Channel save(Channel channel) {
		em.persist(channel);
		return channel;
	}

	@Override
	public Channel findById(long id) {
		Channel channel = em.find(Channel.class, id);
		return channel;
	}
	
}
