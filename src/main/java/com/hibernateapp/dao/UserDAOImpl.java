    package com.hibernateapp.dao;


    import com.hibernateapp.exception.*;
    import com.hibernateapp.model.User;
    import org.hibernate.Session;
    import org.hibernate.SessionFactory;
    import org.hibernate.Transaction;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import com.hibernateapp.util.HibernateSessionFactoryUtil;

    import java.util.List;
    import java.util.Optional;

    public class UserDAOImpl implements UserDAO{

        private static final Logger log = LoggerFactory.getLogger(UserDAOImpl.class);

        private final SessionFactory sessionFactory;

        public UserDAOImpl() {
            this(new HibernateSessionFactoryUtil().getSessionFactory());
        }

        public UserDAOImpl(SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
        }

        public Optional<User> findById(Long id) {
            try(Session session = sessionFactory.openSession()) {
                User user = session.find(User.class, id);
                return Optional.ofNullable(user);
            }
        }

        public User save(User user) {
            try(Session session = sessionFactory.openSession()) {
                Transaction tx = session.beginTransaction();
                try {
                    session.persist(user);
                    tx.commit();
                }
                catch (Exception e) {
                    if(tx != null && tx.isActive()) {
                        tx.rollback();
                    }
                    throw e;
                }
            } catch (Exception e){
                log.error("Failed to create user {} : {} ", user.getName(), e.getMessage());
                throw new UserCreateException("Failed to create user " + user.getName(), e);
            }
            return user;
        }

        public User update(Long id, User user) {
            User managedUser = null;
            try (Session session = sessionFactory.openSession()) {
                Transaction tx = session.beginTransaction();

                try{
                    managedUser = session.find(User.class, id);
                    if(managedUser == null){
                        throw new UserNotFoundException("User with id = " + id + " not found during update attempt");
                    }

                    if(user.getName() != null) managedUser.setName(user.getName());
                    if(user.getEmail() != null) managedUser.setEmail(user.getEmail());
                    if(user.getAge() != null) managedUser.setAge(user.getAge());

                    tx.commit();
                }
                catch (Exception e){
                    if(tx != null && tx.isActive()) {
                        tx.rollback();
                    }
                    throw e;
                }
            }
            catch (UserNotFoundException e) {
                throw e;
            }
            catch (Exception e){
                log.error("Failed to update user with id = {} : {} ", id, e.getMessage());
                throw new UserUpdateException("Failed to update user with id" + id, e);
            }
            return managedUser;
        }

        public void delete(Long id) {
            try(Session session = sessionFactory.openSession()){
                Transaction tx = session.beginTransaction();
                try {
                    User managedUser = session.find(User.class, id);
                    if(managedUser == null){
                        throw new  UserNotFoundException("User with id = " + id + " not found during deletion");
                    }
                    session.remove(managedUser);
                    tx.commit();
                }catch (Exception e){
                    if(tx != null && tx.isActive()) {
                        tx.rollback();
                    }
                    throw e;
                }
            }
            catch (UserNotFoundException e) {
                throw e;
            }
            catch (Exception e){
                log.error("Failed to delete user with id = {} : {} ", id, e.getMessage());
                throw new UserDeleteException("Failed to delete user with id " + id, e);
            }
        }


        public List<User> findAll() {
            List<User> users;
            try (Session session = sessionFactory.openSession()) {
                users = session.createQuery("from User", User.class).getResultList();
            }
            catch (Exception e){
                log.error("Failed to find all users :{} ", e.getMessage());
                throw new UsersRetrievingException("Failed to find all users", e);
            }
            return users;
        }
    }