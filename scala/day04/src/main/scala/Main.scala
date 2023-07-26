import zio._
import userUtil.UserUtil.{readTextFile, getNewLine}

object Main extends ZIOAppDefault {
  override def run = for {
    newLine <- getNewLine()

    file <- readTextFile("day04.txt")

    pairs = (for {
      line <- file.split(newLine)
      sections = for {
        section <- line.split(',').toList
      } yield section
      ranges = for {
        section <- sections
        range = section.split('-').toList match {
          case List(s1, s2) => s1.toInt to s2.toInt
        }
      } yield range
      result = ranges match {
        case List(r1, r2) => (r1, r2)
      }
    } yield result).toList

    fullyContains = for {
      pair <- pairs
      set1 = pair._1.toSet
      set2 = pair._2.toSet
      union = set1 | set2
      result = union == set1 || union == set2
    } yield result

    _ <- Console.printLine(
      s"${fullyContains.count(p => p)} pairs satisfy the condition."
    )

    overlaps = for {
      pair <- pairs
      set1 = pair._1.toSet
      set2 = pair._2.toSet
      union = set1 | set2
      result = set1.size + set2.size > union.size
    } yield result

    _ <- Console.printLine(
      s"${overlaps.count(p => p)} pairs satisfy the condition."
    )

  } yield ()
}
