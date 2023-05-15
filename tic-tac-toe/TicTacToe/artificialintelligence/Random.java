package artificialintelligence;

import tictactoe.Board;

/**
 * Plays a random move in Tic Tac Toe.
 */
class Random {

    private static final java.util.Random utilRandom = new java.util.Random();

    /**
     * Random cannot be instantiated.
     */
    private Random() {
    }


    /**
     * Execute the algorithm.
     *
     * @param board the Tic Tac Toe board to play on
     */
    static void run(Board board) {
        int[] moves = new int[board.getAvailableMoves().size()];
        int index = 0;

        for (Integer item : board.getAvailableMoves()) {
            moves[index++] = item;
        }

        int randomMove = moves[utilRandom.nextInt(moves.length)];
        board.move(randomMove);
    }

}
