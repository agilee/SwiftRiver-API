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
package com.ushahidi.swiftriver.core.api.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.ushahidi.swiftriver.core.api.dao.AccountDao;
import com.ushahidi.swiftriver.core.model.Account;

@Repository
public class JpaAccountDao extends AbstractJpaDao<Account> implements
		AccountDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ushahidi.swiftriver.core.data.dao.AccountDao#findByUsername(java.
	 * lang.String)
	 */
	public Account findByUsername(String username) {
		String query = "SELECT a FROM Account a JOIN a.owner o WHERE o.username = :username";

		Account account = null;
		try {
			account = (Account) em.createQuery(query)
					.setParameter("username", username).getSingleResult();
		} catch (NoResultException e) {
			// Do nothing
		}
		return account;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ushahidi.swiftriver.core.api.dao.AccountDao#findByName(java.lang.
	 * String)
	 */
	public Account findByName(String accountPath) {
		String query = "SELECT a FROM Account a WHERE a.accountPath = :account_path";

		Account account = null;
		try {
			account = (Account) em.createQuery(query)
					.setParameter("account_path", accountPath)
					.getSingleResult();
		} catch (NoResultException e) {
			// Do nothing;
		}
		return account;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ushahidi.swiftriver.core.api.dao.AccountDao#decreaseRiverQuota(com
	 * .ushahidi.swiftriver.core.model.Account, int)
	 */
	public void decreaseRiverQuota(Account account, int decrement) {
		account.setRiverQuotaRemaining(account.getRiverQuotaRemaining()
				- decrement);
		update(account);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ushahidi.swiftriver.core.api.dao.AccountDao#search(java.lang.String)
	 */
	public List<Account> search(String query) {
		String sql = "SELECT a FROM Account a WHERE a.accountPath like :q or a.owner.name like :q or a.owner.email like :q";

		List<Account> accounts = null;
		try {
			accounts = em.createQuery(sql).setParameter("q", query + "%")
					.setMaxResults(10).getResultList();
		} catch (NoResultException e) {
			// Do nothing;
		}
		return accounts;
	}
}
