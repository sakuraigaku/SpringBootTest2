package com.example.demo.login.domain.repository.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.repository.UserDao;

@Repository("UserDaoNamedJdbcImpl")
public class UserDaoNamedJdbcImpl implements UserDao {
	@Autowired
	private NamedParameterJdbcTemplate jdbc;

	@Override
	public int count() throws DataAccessException {
		// TODO 自動生成されたメソッド・スタブ
		String sql="select count(*) from m_user";
		SqlParameterSource params=new MapSqlParameterSource();
		return jdbc.queryForObject(sql, params, Integer.class);
	}

	@Override
	public int insertOne(User user) throws DataAccessException {
		String sql="insert into m_user(user_id,"
				+ "password,"
				+ "user_name,"
				+ "birthday,"
				+ "age,"
				+ "marriage"
				+ "role)"
				+ "values(:userId,"
				+ ":userName,"
				+ ":birthday,"
				+ ":age,"
				+ ":marriage,"
				+ ":role)";
		SqlParameterSource params=new MapSqlParameterSource()
				.addValue("userId", user.getUserId())
				.addValue("password", user.getPassword())
				.addValue("userName", user.getUserName())
				.addValue("birthday", user.getBirthday())
				.addValue("age", user.getAge())
				.addValue("marriage", user.isMarriage())
				.addValue("role", user.getRole());
		return jdbc.update(sql, params);
	}

	@Override
	public User selectOne(String userId) throws DataAccessException {
		String sql="select * from m_user where user_id=:userId";
		SqlParameterSource params=new MapSqlParameterSource()
				.addValue("userId", userId);
		Map<String, Object>map=jdbc.queryForMap(sql, params);
		User user=new User();
		user.setUserId((String)map.get("user_id"));
		user.setPassword((String)map.get("password"));
		user.setUserName((String)map.get("user_name"));
		user.setBirthday((Date)map.get("birthday"));
		user.setAge((Integer)map.get("age"));
		user.setMarriage((Boolean)map.get("marriage"));
		user.setRole((String)map.get("role"));
		return user;
	}

	@Override
	public List<User> selectMany() throws DataAccessException {
		String sql="select * from m_user";
		SqlParameterSource params=new MapSqlParameterSource();
		List<Map<String, Object>>getList=jdbc.queryForList(sql, params);
		List<User>userList=new ArrayList<>();
		for(Map<String, Object>map:getList) {
			User user =new User();
			user.setUserId((String)map.get("user_id"));
			user.setPassword((String)map.get("password"));
			user.setUserName((String)map.get("user_name"));
			user.setBirthday((Date)map.get("birthday"));
			user.setAge((Integer)map.get("age"));
			user.setMarriage((Boolean)map.get("marriage"));
			user.setRole((String)map.get("role"));
			userList.add(user);
		}
		return userList;
	}

	@Override
	public int updateOne(User user) throws DataAccessException {
		String sql="update m_user"
				+ " set"
				+ " password=:password,"
				+ " user_name=:userName,"
				+ " birthday=:birthday,"
				+ " age=:age,"
				+ " marriage=:marriage"
				+ " where user_id=:userId";

		SqlParameterSource params=new MapSqlParameterSource()
				.addValue("userId", user.getUserId())
				.addValue("password", user.getPassword())
				.addValue("userName", user.getUserName())
				.addValue("birthday", user.getBirthday())
				.addValue("age", user.getAge())
				.addValue("marriage", user.isMarriage());

		return jdbc.update(sql, params);
	}

	@Override
	public int deleteOne(String userId) throws DataAccessException {
		String sql="delete from m_user where user_id=:userId";
		SqlParameterSource params=new MapSqlParameterSource()
				.addValue("userId", userId);
		int rowNumber=jdbc.update(sql, params);
		return rowNumber;
	}

	@Override
	public void userCsvOut() throws DataAccessException {
		String sql="select * from m_user";
		UserRowCallbackHandler handler=new UserRowCallbackHandler();
		jdbc.query(sql, handler);
	}

}
