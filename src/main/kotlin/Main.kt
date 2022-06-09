import controll.Controller
import model.Model

fun main() {
    println(
        "w,a,s,d - Move\n" +
                "pause - Pause game\n" +
                "exit - Quit game\n"
    )
    val model = Model()
    ConsoleUI(model)
    Controller(model)
}