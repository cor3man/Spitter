package com.habuma.spitter.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.habuma.spitter.domain.Spitter;
import com.habuma.spitter.domain.Spittle;

public class JdbcSpitterDao implements SpitterDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private static final String SQL_INSERT_SPITTER = "insert into spitter (username, password, fullname) "
			+ "values (?, ?, ?)";

	private static final String SQL_UPDATE_SPITTER = "update spitter set username = ?, password = ?, fullname = ?"
			+ "where id = ?";

	private static final String SQL_SELECT_SPITTER = "select id, username, password, fullname from spitter";

	private static final String SQL_SELECT_SPITTER_BY_ID = SQL_SELECT_SPITTER + " where id=?";

	private static final String SQL_SELECT_SPITTER_BY_NAME = SQL_SELECT_SPITTER + " where username=?";

	private static final String SQL_INSERT_SPITTLE = ""
			+ "insert into spittle (spitter_id, spittleText, postedTime) values (?, ?, ?)";

	private static final String SQL_SELECT_SPITTLE = "select id, spitter_id, spittleText, postedTime from spittle";

	private static final String SQL_SELECT_SPITTLE_FOR_SPITTER = SQL_SELECT_SPITTLE + " where spitter_id=?";
	
	private static final String SQL_SELECT_RECENT_SPITTLE = SQL_SELECT_SPITTLE
			+ " where postedTime > ? order by postedTime desc";

	public Spitter getSpitterById(long id) {
		return jdbcTemplate.queryForObject(SQL_SELECT_SPITTER_BY_ID, new Object[] { id }, new RowMapper<Spitter>() {
			public Spitter mapRow(ResultSet rs, int rowNum) throws SQLException {
				Spitter spitter = new Spitter();
				spitter.setId(rs.getLong(1));
				spitter.setUsername(rs.getString(2));
				spitter.setPassword(rs.getString(3));
				spitter.setFullName(rs.getString(4));
				return spitter;
			}
		});
	}

	public void addSpitter(Spitter spitter) {
		jdbcTemplate.update(SQL_INSERT_SPITTER,
				new Object[] { spitter.getUsername(), spitter.getPassword(), spitter.getFullName() });
		spitter.setId(queryForIdentity());
	}

	public void saveSpitter(Spitter spitter) {
		jdbcTemplate.update(SQL_UPDATE_SPITTER,
				new Object[] { spitter.getUsername(), spitter.getPassword(), spitter.getFullName(), spitter.getId() });
	}

	public void saveSpittle(Spittle spittle) {
		jdbcTemplate.update(SQL_INSERT_SPITTLE,
				new Object[] { spittle.getSpitter().getId(), spittle.getText(), new Date() });
	}

	public List<Spittle> getRecentSpittle() {
		DateTime dt = new DateTime().minusDays(100000);
		return jdbcTemplate.query(SQL_SELECT_RECENT_SPITTLE, new RowMapper<Spittle>() {
			public Spittle mapRow(ResultSet rs, int rowNum) throws SQLException {
				Spittle spittle = new Spittle();

				spittle.setId(rs.getLong(1));
				spittle.setSpitter(getSpitterById(rs.getLong(2)));
				spittle.setText(rs.getString(3));
				spittle.setWhen(rs.getDate(4));
				return spittle;
			}
		}, dt.toDate());
	}

	@SuppressWarnings("deprecation")
	private long queryForIdentity() {
		// return jdbcTemplate.queryForLong("call identity()");
		return jdbcTemplate.queryForLong("call identity()");
	}

	public List<Spittle> getSpittlesForSpitter(Spitter spitter) {
		return jdbcTemplate.query(SQL_SELECT_SPITTLE_FOR_SPITTER, new Object[]{spitter.getId()}, new RowMapper<Spittle>() {

			public Spittle mapRow(ResultSet rs, int rowNum) throws SQLException {
				Spittle spittle = new Spittle();
				spittle.setId(rs.getLong(2));
				spittle.setText(rs.getString(3));
				spittle.setWhen(rs.getDate(4));
				return spittle;
			}
		});
	}

	public Spitter getSpitterByUsername(String username) {
		return jdbcTemplate.queryForObject(SQL_SELECT_SPITTER_BY_NAME, new Object[] { username },
				new RowMapper<Spitter>() {
					public Spitter mapRow(ResultSet rs, int rowNum) throws SQLException {
						Spitter spitter = new Spitter();
						spitter.setId(rs.getLong(1));
						spitter.setUsername(rs.getString(2));
						spitter.setPassword(rs.getString(3));
						spitter.setFullName(rs.getString(4));
						return spitter;
					}
				});
	}

	public void deleteSpittle(long id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public Spittle getSpittleById(long id) {
		return null;
	}

	public List<Spitter> findAllSpitters() {
		// TODO Auto-generated method stub
		return null;
	}
}