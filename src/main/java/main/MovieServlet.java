package main;

import com.google.gson.Gson;
import data.Movie;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import data.MoviesDao;
import data.DaoFactory;

//  localhost:8080/movies/*

@WebServlet(name = "MovieServlet", urlPatterns = "/movies/*")
public class MovieServlet extends HttpServlet {
//                Movie GetOut = new Movie("Get Out",
//                        7.7,
//                        "poster",
//                        2017,
//                        "Horror/Thriller ",
//                        "Jordan Peele",
//                        "Now that Chris and his girlfriend, Rose, have reached the meet-the-parents milestone of dating, she invites him for a weekend getaway upstate with her parents, Missy and Dean.",
//                        " Daniel Kaluuya, Allison Williams, Bradley Whitford", 1);

    ArrayList<Movie> movies = new ArrayList<>();
    int nextId = 1;

     MoviesDao moviesDao;

     public MovieServlet(){
         this.moviesDao = DaoFactory.getMoviesDao(DaoFactory.DaoType.IN_MEMORY);
     }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        try {
            PrintWriter out = response.getWriter();
            List<Movie> moviesArray = moviesDao.all();
            Gson gson = new Gson();
            String json = gson.toJson(moviesArray);
            out.println(json);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        BufferedReader bReader = request.getReader();
        Movie[] newMovies = new Gson().fromJson(bReader, Movie[].class);

        for (Movie movie : newMovies) {
            movie.setId(nextId++);
            movies.add(movie);
        }
        try {
            PrintWriter out = response.getWriter();
            out.println("Movie(s) added");
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader bReader = req.getReader();
        Movie updatedMovie = new Gson().fromJson(bReader, Movie.class);
        resp.setContentType("application/json");
        String[] uriParts = req.getRequestURI().split("/");

        int targetId = Integer.parseInt(uriParts[uriParts.length - 1]);

        for (Movie movie : movies) {
            if (movie.getId() == targetId) {
                int index = movies.indexOf(movie);
                movies.set(index, updatedMovie);
                PrintWriter out = resp.getWriter();
                out.println("Movie updated");
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");

        int targetId = 0;
        try {
            try {
                String [] uriParts = request.getRequestURI().split("/");
                targetId = Integer.parseInt(uriParts[uriParts.length - 1]);
            } catch (Exception e) {}
            int finalTargetId = targetId;
            movies.removeIf(movie -> movie.getId() == finalTargetId);
            PrintWriter out = response.getWriter();
            out.println("Movie deleted");
        } catch (IOException e) {
            System.out.println(e);
        }
    }


}