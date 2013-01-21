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
package com.ushahidi.swiftriver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ushahidi.swiftriver.dao.LinkDAO;
import com.ushahidi.swiftriver.dao.SwiftRiverDAO;
import com.ushahidi.swiftriver.model.Link;
import com.ushahidi.swiftriver.service.LinkService;

/**
 * Service class for links
 * @author ekala
 *
 */
@Service
public class LinkServiceImpl extends AbstractServiceImpl<Link, Long> implements LinkService {

	@Autowired
	private LinkDAO linkDAO;

	public void setLinkDAO(LinkDAO linkDAO) {
		this.linkDAO = linkDAO;
	}

	public Link findByHash(String hash) {
		return linkDAO.findByHash(hash);
	}

	public SwiftRiverDAO<Link, Long> getServiceDAO() {
		return linkDAO;
	}

}
