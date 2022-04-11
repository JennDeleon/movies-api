package main;

import DAO.DaoFactory;
import DAO.MoviesDao;
import DAO.MoviesDaoFactory;
import com.google.gson.Gson;
import data.Movie;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import static java.lang.System.out;

@WebServlet(name = "MovieServlet", urlPatterns = "/movies/*")
public class MovieServlet extends HttpServlet {
private MoviesDao dao = MoviesDaoFactory.getMoviesDao(MoviesDaoFactory.DAOType.MYSQL);
    @Override
    protected void
    doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        try{
            PrintWriter out = response.getWriter();
            out.println(new Gson().toJson(
                    dao
                    .all()));
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void
    doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Movie[] movies = new Gson().fromJson(request.getReader(), Movie[].class);
        for (Movie movie : movies) {
            System.out.println(movie.getTitle());
            System.out.println(movie.getPlot());
            System.out.println(movie.getId());
            System.out.println(movie.getGenre());
            System.out.println(movie.getRating());
            System.out.println(movie.getActors());
            System.out.println(movie.getYear());
            System.out.println(movie.getDirector());
        }
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
           dao.insertAll(movies);
        } catch (Exception e) {
            out.println(new Gson().toJson(e.getLocalizedMessage()));
            response.setStatus(500);
            e.printStackTrace();
            return;
        }
        out.println(new Gson().toJson(" {message: \"Movies POST was successful\"}"));
        response.setStatus(200);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String [] uriParts = request.getRequestURI().split("/");
        int targetId = Integer.parseInt(uriParts[uriParts.length - 1]);
        try {
            Movie movie = new Gson().fromJson(request.getReader(), Movie.class);
            movie.setId(targetId);
            dao.update(movie);
        } catch (SQLException e) {
            out.println(new Gson().toJson(e.getLocalizedMessage()));
            response.setStatus(500);
            e.printStackTrace();
            return;
        } catch (Exception e) {
            out.println(new Gson().toJson(e.getLocalizedMessage()));
            response.setStatus(400);
            e.printStackTrace();
            return;
        }

        out.println(new Gson().toJson("{message: \"Movie UPDATE was successful\"}"));
        response.setStatus(200);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String [] uriParts = request.getRequestURI().split("/");
        int targetId = Integer.parseInt(uriParts[uriParts.length - 1]);
        try {
            dao.delete(targetId);
        } catch (SQLException e) {
            out.println(new Gson().toJson(e.getLocalizedMessage()));
            response.setStatus(500);
            e.printStackTrace();
            return;
        }

        out.println(new Gson().toJson("{message: \"Movie DELETE was successful\"}"));
        response.setStatus(200);
    }
}
