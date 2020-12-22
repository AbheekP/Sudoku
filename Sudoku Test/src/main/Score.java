package main;

public class Score {

	private int incorrect;
	private int minutes;
	private int seconds;
	private int penaltyScore;
	private String time;
	private String date;
	private int penalty = 10;
	private String difficulty;
	
	public Score(String incorrect, String minutes, String seconds, String time, String date, String difficulty) {
		
		this.incorrect = Integer.parseInt(incorrect);
		this.minutes = Integer.parseInt(minutes);
		this.seconds = Integer.parseInt(seconds);
		this.time = time;
		this.date = date;
		this.difficulty = difficulty;
		
		int difficultyPenalty = 0;
		
		if(difficulty.equals("easy")) {
			
			difficultyPenalty = 3;
			
		} else if (difficulty.equals("medium")) {
			
			difficultyPenalty = 2;
			
		} else if (difficulty.equals("hard")){
			
			difficultyPenalty = 1;
			
		} else {
			
			throw new IllegalArgumentException("Invalid Difficulty");
			
		}
		
		/*
		 * Lower penalty score means better attempt.
		 * Leader board is ranked from first to last based on lowest to highest penalty score.
		 * 
		 * - 1 second is +1 penalty score/
		 * - 1 incorrect answer is +10 penalty score.
		 * - total incorrect points = (time penalty score + incorrect penalty score) * difficulty penalty value
		 * 		- easy has a difficulty penalty value of 3.
		 * 		- medium has a difficulty penalty value 2.
		 * 		- hard has a difficulty penalty value of 1.
		 * 
		 */
		this.penaltyScore = ((Integer.parseInt(minutes) * 60) + Integer.parseInt(seconds) + (Integer.parseInt(incorrect) * penalty)) * difficultyPenalty;
		
	}

	public int getIncorrect() {
		return incorrect;
	}

	public int getMinutes() {
		return minutes;
	}

	public int getSeconds() {
		return seconds;
	}

	public int getPenaltyScore() {
		return penaltyScore;
	}

	public String getTime() {
		return time;
	}

	public String getDate() {
		return date;
	}

	public String getDifficulty() {
		return difficulty;
	}
	
	
	
}
