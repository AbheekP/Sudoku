# Sudoku

This is a Sudoku game created using Java, Java AWT, and Java Swing. It features randomly generated Sudoku boards, difficulty levels, and a high score system. 

# Instructions 

When the application is started, a simple menu screen will be presented. 

![image](https://user-images.githubusercontent.com/76227825/102935434-21dda100-4474-11eb-8530-fb4139b04bbf.png)

### "Start Game"

Pressing start game will open up another window with the Sudoku board. At the top left of the frame, there will be a timer that shows how much time has been spent on the current Sudoku board, and on the top right there will be a counter for how many mistakes were made while solving the current Sudoku board. Below that is the actual Sudoku board where answers are inputted. 

![image](https://user-images.githubusercontent.com/76227825/102935949-248cc600-4475-11eb-96fb-a3967656034b.png)

At the bottom, of the frame, there are numbers ranging from 1 - 9. Clicking on these numbers will change the input number to whatever number was clicked. The current input number will be highlighted with a green border. Clicking an empty cell on the Sudoku board will result in the selected input number being placed into that cell. If the number is correct, the cell will be highlighted in green, causing the cell to be "solved". Otherwise, the cell will turn red and the incorrect counter will increase by one. In order to make playing the game easier, clicking an input number will cause all solved cells with that number to be highlighted in green as well. 

![image](https://user-images.githubusercontent.com/76227825/102936455-36bb3400-4476-11eb-9e17-b9bee17176aa.png)

If a number is complete (meaning no more cells require the number), then the input number button will dissappear. Note: There may be cases where a number is already complete before the board is presented to the user. This often happens when the difficulty is set on easy. 

![image](https://user-images.githubusercontent.com/76227825/102937031-4129fd80-4477-11eb-8934-aa7d8ad1243f.png)

Below the input numbers are 2 buttons. The "Reset Wrong Answers" button will cause all incorrect cells to turn back to an empty white cell. The "Return to Menu" button will return to the main menu. Pressing this button will cause the next board to be different. 

Upon completing the board, A popup will display the time it took to solve the board and the amount of incorrect guesses the user inputted. The user can then chose to exit or return to the main menu. Returning to the main menu will cause the next board to be different. 

![image](https://user-images.githubusercontent.com/76227825/102937338-ed6be400-4477-11eb-8c69-4174ecc5b5f2.png)

### "Settings" 

Pressing the settings button will open up another window where the user can change the difficulty of the Sudoku board. Pressing one of the buttons will change the difficulty respectively. The number besides the difficulty level on the button states how many cells will be visible out of 81 total cells. 

![image](https://user-images.githubusercontent.com/76227825/102937702-c2ce5b00-4478-11eb-9baf-7fb21de8c0bb.png)

The default difficulty is easy. Every time a "return to main menu" button is pressed, the difficulty is reset to the default difficulty. 

### "High Scores"

Pressing the high scores button will open a window that shows the user's top 10 best attempts for solving a sudoku board. Each listing will show the amount of incorrect guesses along with the difficulty level (in brackets) for that run, the time taken to solve that board, and the time/date the run was completed.

![image](https://user-images.githubusercontent.com/76227825/102939232-e5ae3e80-447b-11eb-8888-e3008387c61b.png)

The rankings are based on a "penalty score", where a lower penalty score is a better run. The penalty score is determined based on 3 factors: difficulty level, incorrect guesses, and time. A higher amount of incorrect guesses and a higher time increases the penalty score, while a harder difficulty effectively decreases the penalty score. 
