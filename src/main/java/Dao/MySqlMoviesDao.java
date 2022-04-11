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

            this.connection = DriverManager.getConnection(
                    config.getUrl(), // <-- WHERE IS THE DB?
                    config.getUser(), // <-- WHO IS ACCESSING?
                    config.getPassword() // <-- WHAT IS THEIR PASSWORD?
            );

        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database!", e);
        }
    }

    @Override
    public List<Movie> all() throws SQLException {

        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM movies";
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
    public Movie findOne(int id) throws SQLException {
        // TODO: Get one movie by id
        String sql = "SELECT * FROM jennifer.movies WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        return null;

    }


    @Override
    public void insert(Movie movie) {
        // TODO: Insert one movie
    }


    public void insertAll(Movie[] movies) throws SQLException {

        // Build sql template
        StringBuilder sql = new StringBuilder("INSERT INTO movies (" +
                "id, title, year, director, actors, imdbId, poster, genre, plot) " +
                "VALUES ");


        // Add a interpolation template for each element in movies list
        sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?), ".repeat(movies.length));

        // Create a new String and take off the last comma and whitespace
        sql = new StringBuilder(sql.substring(0, sql.length() - 2));

        // Use the sql string to create a prepared statement
        PreparedStatement statement = connection.prepareStatement(sql.toString());

        // Add each movie to the prepared statement using the index of each sql param: '?'
        // This is done by using a counter
        // You could use a for loop to do this as well and use its incrementor
        int counter = 0;
        for (Movie movie : movies) {
            statement.setInt((counter * 9) + 1, movie.getId());
            statement.setString((counter * 9) + 2, movie.getTitle());
            statement.setInt((counter * 9) + 3, movie.getYear());
            statement.setString((counter * 9) + 4, movie.getDirector());
            statement.setString((counter * 9) + 5, movie.getActors());
            statement.setDouble((counter * 9) + 6, movie.getRating());
            statement.setString((counter * 9) + 7, movie.getPoster());
            statement.setString((counter * 9) + 8, movie.getGenre());
            statement.setString((counter * 9) + 9, movie.getPlot());
            counter++;
        }
        statement.executeUpdate();
    }


    @Override
    public void update(Movie movie) throws SQLException {
        //TODO: Update a movie here!
    }

    @Override
    public void delete(int id) throws SQLException {
        //TODO: Annihilate a movie
    }
    @Override
    public void destroy(int id) throws SQLException {

        String sql =
                "DELETE FROM movies " +
                        "WHERE id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, id);

        statement.execute();
    }

}

