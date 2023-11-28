# Assignment 3
In this assignment, you will use built in Java data structures to count top artists on Spotify.

### Data
We have included the dataset from this Kaggle site: https://www.kaggle.com/datasets/jfreyberg/spotify-chart-data

### Setup command
N/A

### Run command
You will need to use command line arguments to run this code. To do so, open up a terminal (in VS Code or in the root directory of the assignment) and use the following command structure:

`gradle run --args "STRATEGY N_ARTISTS MAX_SONGS"`
        where:
                STRATEGY is one of "list", "tree", "hash" (required)
                N_ARTISTS is an integer specifying the number of songs used in the analysis (optional, defaults to 20)
                MAX_SONGS is an integer specifying the number of songs used in the analysis (optional, defaults to the entire list)
        example:
                `gradle run --args "hash 20 50000"`

### Test command
You may optionally use unit tests as you program. Run `gradle test` to run this code.
