import zio._
import userUtil.UserUtil.{getNewLine, readTextFile}
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.ListBuffer

object Main extends ZIOAppDefault {
  def parseContent(content: String): List[Option[Char]] = {
    var input = content
    var result = ArrayBuffer[Option[Char]]()

    while (input.nonEmpty) {
      val part = input.take(3)

      if (part.isBlank()) {
        result ++= List(None)
      } else {
        result ++= List(Some(part.charAt(1)))
      }

      input = input.drop(3)

      if (input.nonEmpty && input.head == ' ') {
        input = input.drop(1)
      }
    }

    return result.toList
  }

  def parseDrawing(drawing: String) = for {
    newLine <- getNewLine()
    lines = drawing.split(newLine).toList
    contents = lines.take(lines.length - 1)

    stackCount = lines.last.split(' ').last.toInt

    parsed = for {
      content <- contents

      result = parseContent(content)

    } yield result

    reversed = parsed.reverse

    result = ArrayBuffer[ListBuffer[Char]]()

    _ = (0 until stackCount).foreach(idx => {
      result.append(ListBuffer())
    })

    _ = (0 until stackCount).foreach(idx => {
      reversed.foreach(list => {
        list(idx) match {
          case Some(value) => result(idx).append(value)
          case None        =>
        }
      })
    })

  } yield result

  def parseProcedure(procedure: String) = for {
    newLine <- getNewLine()
    lines = procedure.split(newLine).toList

    results = for {
      line <- lines
      tokens = line.split(' ').toList
      values = tokens.drop(1).grouped(2).map(_.head).toList

      numbers = for {
        value <- values
      } yield value.toInt

      result = numbers match {
        case List(i1, i2, i3) => (i1, i2 - 1, i3 - 1)
      }
    } yield result

  } yield results

  def process(
      stacks: ArrayBuffer[ListBuffer[Char]],
      rearrangements: List[(Int, Int, Int)],
      is9001: Boolean
  ): List[ListBuffer[Char]] = {
    var result = stacks

    rearrangements.foreach(rearrangement => {
      val (quantity, from, to) = rearrangement
      val crates = if (is9001) {
        result(from).takeRight(quantity)
      } else {
        result(from).takeRight(quantity).reverse
      }

      result(to) ++= crates

      (quantity to 1 by -1).foreach(i => {
        stacks(from).remove(stacks(from).length - i)
      })
    })

    return result.toList
  }

  override def run = for {
    newLine <- getNewLine()
    file <- readTextFile("day05.txt")

    input = file.split(newLine * 2).toList
    drawing = input.head
    procedure = input.last

    parsedProcedure <- parseProcedure(procedure)

    // Somehow .clone() is doing a shallow copy
    // Doing some duplicated job for workaround

    crate1 <- parseDrawing(drawing)

    processed9000 = process(crate1, parsedProcedure, false)

    part1 = for {
      stack <- processed9000
    } yield stack.last

    _ <- Console.printLine(s"Part 1: Top of each stack: ${part1.mkString}")

    crate2 <- parseDrawing(drawing)

    processed9001 = process(crate2, parsedProcedure, true)

    part2 = for {
      stack <- processed9001
    } yield stack.last

    _ <- Console.printLine(s"Part 2: Top of each stack: ${part2.mkString}")

  } yield ()
}
