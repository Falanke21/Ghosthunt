package fall2018.csc2017.game_centre;

/**
 * Model class, excluded from unit test.
 * Keep track of various status.
 */
public class CurrentStatus {

    /**
     * Current user.
     */
    private static User currentUser;

    /**
     * Set current user.
     * @param u current user
     */
    static void setCurrentUser(User u) {
        currentUser = u;
    }

    /**
     * Get current user.
     * @return current user
     */
    public static User getCurrentUser() {
        return currentUser;
    }
}
