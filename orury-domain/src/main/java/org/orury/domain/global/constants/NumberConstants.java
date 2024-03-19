package org.orury.domain.global.constants;

public final class NumberConstants {
    private NumberConstants() {
    }

    // User Id Hard Coding (Before Adapting Security)
    public static final Long USER_ID = 1L;
    // First Pagination Cursor Input Value
    public static final Long FIRST_CURSOR = 0L;
    // Last Pagination Cursor Return Value
    public static final Long LAST_CURSOR = -1L;
    // First Page But Nothing Cursor Return Value
    public static final Long NOTHING_CURSOR = -2L;
    // Post pagination Size
    public static final int POST_PAGINATION_SIZE = 10;
    // Comment pagination Size
    public static final int COMMENT_PAGINATION_SIZE = 10;
    // Review pagination Size
    public static final int REVIEW_PAGINATION_SIZE = 10;
    // Gym pagination Size
    public static final int GYM_PAGINATION_SIZE = 15;
    // Crew pagination Size
    public static final int CREW_PAGINATION_SIZE = 10;
    // notification pagination Size
    public static final int NOTIFICATION_PAGINATION_SIZE = 10;
    // int Value of Not Deleted Object`s Status
    public static final int IS_NOT_DELETED = 0;
    // int Value of Deleted Object`s Status
    public static final int IS_DELETED = 1;
    // long Value of P
    public static final long PARENT_COMMENT = 0L;
    // Last Pagination Page Return Value
    public static final int LAST_PAGE = -1;
    // Minimun LikeCount for Hot Posts
    public static final int HOT_POSTS_BOUNDARY = 3;
    // Review Reaction
    public static final int NOT_REACTION = 0;
    public static final int THUMB_REACTION = 1;
    public static final int INTERREST_REACTION = 2;
    public static final int HELP_REACTION = 3;
    public static final int LIKE_REACTION = 4;
    public static final int ANGRY_REACTION = 5;
    // Maximum Number of Crew Participation
    public static final int MAXIMUM_CREW_PARTICIPATION = 3;
    // Maximum Number of User Profile Images from Thumbnail of Meetings List
    public static final int MAXIMUM_OF_MEETING_THUMBNAILS = 10;
    // Maximum Number of User Profile Images from Thumbnail of Crews List
    public static final int MAXIMUM_OF_CREW_THUMBNAILS = 4;
    // Minimum Number of Meeting Capacity
    public static final int MINIMUM_MEETING_CAPACITY = 2;


    //default values
    public static final long DELETED_USER_ID = -9999L;
}
