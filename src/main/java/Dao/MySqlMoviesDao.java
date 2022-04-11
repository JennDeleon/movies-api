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

    public List<Movie> all() throws SQLException {
        ArrayList<Movie> movies = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet movieList = statement.executeQuery("select * from jennifer.movies");
        while (movieList.next()){
            movies.add(new Movie(
                    movieList.getString("title"),
                    movieList.getDouble("rating"),
                    movieList.getString("poster"),
                    movieList.getInt("year"),
                    movieList.getString("genre"),
                    movieList.getString("director"),
                    movieList.getString("plot"),
                    movieList.getString("actors"),
                    movieList.getInt("id")
            ));
        }
        movieList.close();
        statement.close();
        return movies;
    }

    @Override
    public Movie findOne(int id) {
        Movie movie = new Movie();
        try {
            PreparedStatement ps = connection.prepareStatement("Select * from movies where id = ?");
            ResultSet rs = ps.executeQuery();
            ps.setInt(1, id);
            rs.next();

            movie.setTitle(rs.getString("title"));
            movie.setYear(rs.getInt("year"));
            movie.setGenre(rs.getString("genre"));
            movie.setRating(rs.getDouble("rating"));
            movie.setDirector(rs.getString("director"));
            movie.setActors(rs.getString("actors"));
            movie.setPlot(rs.getString("plot"));
            movie.setPoster(rs.getString("poster"));

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movie;
    }


    public void insert(Movie movie) throws SQLException {
        // TODO: Insert one movie
        PreparedStatement pStatement = connection.prepareStatement("insert into jennifer.movies " +
                " (title, year, genre, director, actors, rating, poster, plot) " +
                " values (?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
        pStatement.setString(1, movie.getTitle());
        pStatement.setInt(2, movie.getYear());
        pStatement.setString(3, movie.getGenre());
        pStatement.setString(4, movie.getDirector());
        pStatement.setString(5, movie.getActors());
        pStatement.setDouble(6, movie.getRating());
        pStatement.setString(7, movie.getPoster());
        pStatement.setString(8, movie.getPlot());
        pStatement.executeUpdate();
        pStatement.close();
    }


    @Override
    public void insertAll(Movie[] movies) throws SQLException {
        for (Movie m : movies) {
            insert(m);
        }
    }


    @Override
    public void update(Movie movie) throws SQLException {
        int currentIndex = 1;
        String qString = "Update movies set";
        if (movie.getTitle() != null) {
            qString += " title = ?,";
        }
        if (movie.getRating() != null) {
            qString += " rating = ?,";
        }
        qString = qString.substring(0,qString.length() - 1);
        qString += " where id = ?";
        PreparedStatement updateStmt = connection.prepareStatement(qString, Statement.RETURN_GENERATED_KEYS);
        if (movie.getTitle() != null) {
            updateStmt.setString(currentIndex, movie.getTitle());
            currentIndex++;
        }
        if (movie.getRating() != null) {
            updateStmt.setDouble(currentIndex, movie.getRating());
            currentIndex++;
        }
        updateStmt.setInt(currentIndex,movie.getId());
        updateStmt.executeUpdate();

        updateStmt.close();
    }

    @Override
    public void delete(int id) throws SQLException {
        PreparedStatement deleteStmt = connection.prepareStatement("Delete from movies where id = " + id);
        deleteStmt.execute();

        deleteStmt.close();
    }


}

