import zio._
import userUtil.UserUtil.readTextFile

object Main extends ZIOAppDefault {
  def isAllUnique(input: List[Char]): Boolean = {
    return input.distinct.size == input.size
  }

  override def run = for {
    file <- readTextFile("day06.txt")

    chars = file.toList

    range1 = (0 until chars.size - 4 - 1)

    marker1 = range1
      .map(i => isAllUnique(chars.slice(i, i + 4)))
      .indexOf(true) + 4

    _ <- Console.printLine(
      s"First start-of-packet marker is detected at $marker1!"
    )

    range2 = (0 until chars.size - 14 - 1)

    marker2 = range2
      .map((i => isAllUnique(chars.slice(i, i + 14))))
      .indexOf(true) + 14

    _ <- Console.printLine(
      s"First start-of-message marker is detected at $marker2!"
    )
  } yield ()
}
