package com.springmvc.repo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.springmvc.pojo.Spittle;

@Repository
public class SpittleRepositoryImpl implements SpittleRepository {

	private JdbcOperations jdbc;

	@Autowired
	public SpittleRepositoryImpl(JdbcOperations jdbc) {
		this.jdbc = jdbc;
	}

	public List<Spittle> findSpittles(long limit, int offset) {
		return jdbc.query("select id, name from t_spittle limit ? offset ?", 
				new SpittleRowMapper(), limit, offset);
	}

	private static class SpittleRowMapper implements RowMapper<Spittle> {
		public Spittle mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Spittle(rs.getInt("id"), rs.getString("name"));
		}
	}

	@Override
	public Spittle findSpittle(int id) {
		return jdbc.queryForObject("select id,name,address from t_spittle where id=?", 
				new RowMapper<Spittle>() { 
					@Override
					public Spittle mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new Spittle(rs.getInt("id"), rs.getString("name"),
								rs.getString("address"));
					}
				}, 
				id);
	}
	
	@Override
	public int getItemSum() {
		return (int) jdbc.queryForObject("select count(*) as item_sum from t_spittle", new RowMapper<Object>() {
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("item_sum");
			}
		});
	}
}
