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
package com.ushahidi.swiftriver.dao.hibernate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ushahidi.swiftriver.dao.IdentityDAO;
import com.ushahidi.swiftriver.model.Identity;

/**
 * Hibernate class for identities
 * @author ekala
 *
 */
@Repository("identityDAO")
@Transactional
public class HibernateIdentityDAO extends AbstractHibernateDAO<Identity, Long> implements IdentityDAO {

	public HibernateIdentityDAO() {
		super(Identity.class);
	}
}
