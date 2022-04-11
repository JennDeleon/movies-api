package DAO;

import Config.Config;
import com.mysql.cj.jdbc.Driver;
import data.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MySqlMoviesDao implements MoviesDao {

    public Connection connection = null;

    public MySqlMoviesDao(Config config) {
        //TODO: We will configure our connections here
        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection(
                    config.getUrl(),
                    config.getUser(),
                    config.getPassword()
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database!, e");
        }
    }

    @Override
    public List<Movie> all() throws SQLException {

        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM jennifer.movies";
        ResultSet rs = statement.executeQuery(sql);

        List<Movie> movies = new ArrayList<>();
        while (rs.next()) {
            Movie m = new Movie();
            m.setId(rs.getInt("id"));
            m.setTitle(rs.getString("title"));
            m.setYear(rs.getInt("year"));
            m.setGenre(rs.getString("genre"));
            m.setRating(rs.getDouble("rating"));
            m.setDirector(rs.getString("director"));
            movies.add(m);
        }
        return movies;
    }

    @Override
    public Movie findOne(int id) {
        // TODO: Get one movie by id
        return null;

    }


    @Override
    public void insert(Movie movie) {
        // TODO: Insert one movie
    }

    public void insertAll(Movie[] movies) throws SQLException {
        // TODO: Insert all the movies!
    }

    @Override
    public void update(Movie movie) throws SQLException {
        //TODO: Update a movie here!
    }

    @Override
    public void delete(int id) throws SQLException {
        //TODO: Annihilate a movie
    }
}

