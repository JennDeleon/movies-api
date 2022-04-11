package DAO;

import Config.Config;

public class DaoFactory {
    private static MoviesDao moviesDao;
    private static Config config = new Config();
    public enum ImplType {MYSQL, IN_MEMORY};
    public static MoviesDao getMoviesDao(ImplType implementationType){
        switch(implementationType){
            case IN_MEMORY:{
                moviesDao = new InMemoryMoviesDao();
            }
            case MYSQL:{
                moviesDao = new MySqlMoviesDao(config);
            }
        }
        return moviesDao;
    }
}
