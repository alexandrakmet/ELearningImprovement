CREATE TABLE image
(
    id  INT PRIMARY KEY,
    src TEXT
);

CREATE TABLE quiz
(
    id              INT PRIMARY KEY,
    name            VARCHAR(50),
    question_number INT,
    image_id        INT/*,
    FOREIGN KEY (image_id)
        REFERENCES image (id)
        ON DELETE CASCADE*/
);

CREATE TABLE question
(
    id            INT PRIMARY KEY,
    quiz_id       INT,
    type ENUM ('select_option', 'select_sequence', 'true_false', 'enter_answer'),
    content       VARCHAR(255),
    score         INT,
    image_id      INT,
    /* FOREIGN KEY (image_id)
         REFERENCES image (id)
         ON DELETE CASCADE,*/
    FOREIGN KEY (quiz_id)
        REFERENCES quiz (id)
        ON DELETE CASCADE
);

CREATE TABLE question_option
(
    id             INT PRIMARY KEY,
    question_id    INT,
    content        VARCHAR(255),
    is_correct     BOOLEAN,
    sequence_order INT,
    image_id       INT,
    /*FOREIGN KEY (image_id)
        REFERENCES image (id)
        ON DELETE CASCADE,*/
    FOREIGN KEY (question_id)
        REFERENCES question (id)
        ON DELETE CASCADE
);

CREATE TABLE game
(
    game_id VARCHAR(25) PRIMARY KEY,
    quiz_id INT,
    host_id INT,
    FOREIGN KEY (quiz_id)
        REFERENCES quiz (id)
        ON DELETE CASCADE
);

CREATE TABLE user
(
    id            INT AUTO_INCREMENT,
    game_id       VARCHAR(255),
    login         VARCHAR(255),
    registered_id INT,
    score         INT,
    is_host       BOOLEAN DEFAULT FALSE,
    combo_answer  INT,
    PRIMARY KEY (id, game_id, login)-- ,
--     FOREIGN KEY (game_id)
--     REFERENCES game (id)
--     ON DELETE CASCADE
);

CREATE TABLE game_questions
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    game_id     VARCHAR(25),
    question_id INT,
    is_current  BOOLEAN DEFAULT FALSE,
    finish_time TIMESTAMP,
    FOREIGN KEY (game_id)
        REFERENCES game (game_id)
        ON DELETE CASCADE,
    FOREIGN KEY (question_id)
        REFERENCES question (id)
        ON DELETE CASCADE
);

CREATE TABLE settings
(
    game_id                  VARCHAR(25),
    time                     INT,
    question_answer_sequence BOOLEAN,
    quick_answer_bonus       BOOLEAN,
    combo                    BOOLEAN,
    intermediate_result      BOOLEAN,
    FOREIGN KEY (game_id)
        REFERENCES game (game_id)
        ON DELETE CASCADE
);

CREATE TABLE answer
(
    id                INT AUTO_INCREMENT PRIMARY KEY,
    user_id           INT,
    question_id       INT,
    game_id           VARCHAR(25),
    percent           INT(3),
    time              TIMESTAMP,
    FOREIGN KEY (user_id)
        REFERENCES user (id)
        ON DELETE CASCADE,
    FOREIGN KEY (question_id)
        REFERENCES question (id)
        ON DELETE CASCADE,
    FOREIGN KEY (game_id)
        REFERENCES game (game_id)
        ON DELETE CASCADE
);