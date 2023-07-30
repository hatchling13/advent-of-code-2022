import zio._
import userUtil.UserUtil.{getNewLine, readTextFile}

object Main extends ZIOAppDefault {
  override def run = for {
    newLine <- getNewLine()
    file <- readTextFile("day05.txt")

    input = file.split(newLine * 2)
    drawing = input.head
    procedure = input.last

  } yield ()
}
