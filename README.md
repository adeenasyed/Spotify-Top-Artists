# MSCI 240 (Data Structures & Algorithms) Assignment 3

### Data
Dataset: https://www.kaggle.com/datasets/jfreyberg/spotify-chart-data

### Run command
`gradle run --args "STRATEGY N_ARTISTS MAX_SONGS"`
        where:
                STRATEGY is one of "list", "tree", "hash" (required)
                N_ARTISTS is an integer specifying the number of songs used in the analysis (optional, defaults to 20)
                MAX_SONGS is an integer specifying the number of songs used in the analysis (optional, defaults to the entire list)
        example:
                `gradle run --args "hash 20 50000"`
