package model

import java.io.File

enum class Cell(private val textValue: String) {
    WALL("#"),
    PLAYER("P"),
    WAY("-"),
    EXIT("E");

    override fun toString(): String {
        return textValue
    }
}

enum class State(private val textValue: String) {
    LEFT("Move left"),
    RIGHT("Move right"),
    UP("Move up"),
    DOWN("Move down"),
    MOVE("Waiting for move."),
    EXIT("Game is interrupted."),
    PAUSE("Game is paused. To continue, run program again."),
    WIN("Game finished. You win!!!");

    override fun toString(): String {
        return textValue
    }
}

interface ModelChangeListener {
    fun onModelChanged()
}

class Model {
    val maze: MutableList<MutableList<Cell>> = createMaze()
    var lengthMaze: Int = 0
    var widthMaze: Int = 0
    private var currentPosition = currentPosition()
    var state: State = State.MOVE
        internal set

    private val listeners: MutableSet<ModelChangeListener> = mutableSetOf()

    fun addModelChangeListener(listener: ModelChangeListener) {
        listeners.add(listener)
    }

    fun removeModelChangeListener(listener: ModelChangeListener) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it.onModelChanged() }
    }

    private fun createMaze(): MutableList<MutableList<Cell>> {
        val file = File("src\\main\\kotlin\\maze1").readLines()
        lengthMaze = file.size
        widthMaze = file[0].length
        val maze = MutableList(lengthMaze) {
            MutableList(widthMaze) { Cell.WAY }
        }

        for (i in 0 until lengthMaze) {
            for (j in 0 until widthMaze) {
                if (file[i][j] == 'P') {
                    maze[i][j] = Cell.PLAYER
                } else if (file[i][j] == 'E') {
                    maze[i][j] = Cell.EXIT
                } else if (file[i][j] == '-') {
                    maze[i][j] = Cell.WAY
                } else {
                    maze[i][j] = Cell.WALL
                }
            }
        }
        return maze
    }

    private fun currentPosition(): Pair<Int, Int> {
        var result = Pair(0, 0)
        for (i in 0 until lengthMaze) {
            for (j in 0 until widthMaze) {
                if (maze[i][j] == Cell.PLAYER) {
                    result = Pair(i, j)
                }
            }
        }
        return result
    }

    fun doMove(state: State): Boolean {
        var isWin = false
        when (state) {
            State.UP -> {
                when (maze[currentPosition.first - 1][currentPosition.second]) {
                    Cell.EXIT -> {
                        maze[currentPosition.first][currentPosition.second] = Cell.WAY
                        maze[currentPosition.first - 1][currentPosition.second] = Cell.PLAYER
                        currentPosition = Pair(currentPosition.first - 1, currentPosition.second)
                        isWin = true
                    }
                    Cell.WAY -> {
                        maze[currentPosition.first][currentPosition.second] = Cell.WAY
                        maze[currentPosition.first - 1][currentPosition.second] = Cell.PLAYER
                        currentPosition = Pair(currentPosition.first - 1, currentPosition.second)
                    }
                    Cell.WALL -> {
                        error("Can't move. It's wall.")
                    }
                    else -> {}
                }
            }
            State.LEFT -> {
                when (maze[currentPosition.first][currentPosition.second - 1]) {
                    Cell.EXIT -> {
                        maze[currentPosition.first][currentPosition.second] = Cell.WAY
                        maze[currentPosition.first][currentPosition.second - 1] = Cell.PLAYER
                        currentPosition = Pair(currentPosition.first, currentPosition.second - 1)
                        isWin = true
                    }
                    Cell.WAY -> {
                        maze[currentPosition.first][currentPosition.second] = Cell.WAY
                        maze[currentPosition.first][currentPosition.second - 1] = Cell.PLAYER
                        currentPosition = Pair(currentPosition.first, currentPosition.second - 1)
                    }
                    Cell.WALL -> {
                        error("Can't move. It's wall.")
                    }
                    else -> {}
                }
            }
            State.DOWN -> {
                when (maze[currentPosition.first + 1][currentPosition.second]) {
                    Cell.EXIT -> {
                        maze[currentPosition.first][currentPosition.second] = Cell.WAY
                        maze[currentPosition.first + 1][currentPosition.second] = Cell.PLAYER
                        currentPosition = Pair(currentPosition.first + 1, currentPosition.second)
                        isWin = true
                    }
                    Cell.WAY -> {
                        maze[currentPosition.first][currentPosition.second] = Cell.WAY
                        maze[currentPosition.first + 1][currentPosition.second] = Cell.PLAYER
                        currentPosition = Pair(currentPosition.first + 1, currentPosition.second)
                    }
                    Cell.WALL -> {
                        error("Can't move. It's wall.")
                    }
                    else -> {}
                }
            }
            State.RIGHT -> {
                when (maze[currentPosition.first][currentPosition.second + 1]) {
                    Cell.EXIT -> {
                        maze[currentPosition.first][currentPosition.second] = Cell.WAY
                        maze[currentPosition.first][currentPosition.second + 1] = Cell.PLAYER
                        currentPosition = Pair(currentPosition.first, currentPosition.second - 1)
                        isWin = true
                    }
                    Cell.WAY -> {
                        maze[currentPosition.first][currentPosition.second] = Cell.WAY
                        maze[currentPosition.first][currentPosition.second + 1] = Cell.PLAYER
                        currentPosition = Pair(currentPosition.first, currentPosition.second + 1)
                    }
                    Cell.WALL -> {
                        error("Can't move. It's wall.")
                    }
                    else -> {}
                }
            }

            State.PAUSE -> {}

            State.EXIT -> {}

            else -> {
                println("Wrong key!!!")
            }
        }

        notifyListeners()

        return isWin
    }

    override fun toString(): String {
        return buildString {
            if (state != State.EXIT && state != State.PAUSE) {
                maze.forEach {
                    append(it).appendLine()
                }
            }
            append(state).appendLine()
        }
    }
}