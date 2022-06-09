package controll

import model.Model
import model.State
import java.io.File

class Controller(private val model: Model) {
    init {
        startGame()
    }

    private fun startGame() {
        while ((model.state != State.EXIT) && (model.state != State.PAUSE) && (model.state != State.WIN)) {
            val input = readln()
            var state = State.MOVE
            var isWin = false
            when (input) {
                "w" -> state = State.UP
                "a" -> state = State.LEFT
                "s" -> state = State.DOWN
                "d" -> state = State.RIGHT
                "exit" -> {
                    state = State.EXIT
                    model.state = State.EXIT
                }
                "pause" -> {
                    state = State.PAUSE
                    model.state = State.PAUSE
                    saveGame()
                }
            }
            try {
                isWin = model.doMove(state)
            } catch (e: Exception) {
                println(e.message)
            }
            if (isWin) {
                model.state = State.WIN
                val fileToRead =
                    File("src\\main\\kotlin\\maze")
                val fileToWrite =
                    File("src\\main\\kotlin\\maze1")
                fileToRead.copyTo(fileToWrite, true)
                println(State.WIN)
            }
        }
    }

    private fun saveGame() {
        val file = File("src\\main\\kotlin\\maze1").bufferedWriter()
        for (i in 0 until model.lengthMaze) {
            for (j in 0 until model.widthMaze) {
                file.write(model.maze[i][j].toString())
            }
            file.newLine()
        }
        file.close()
    }
}