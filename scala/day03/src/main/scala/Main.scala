import zio._
import userUtil.UserUtil.{readTextFile, getNewLine}

object Main extends ZIOAppDefault {
  def appearanceSet(rucksack: String): Set[Char] = {
    var frequency = Set[Char]()

    rucksack.foreach(item => {
      frequency += item
    })

    return frequency
  }

  def convert(char: Char): Int = {
    return char.isLower match {
      case true  => char.toInt - 'a'.toInt + 1
      case false => char.toInt - 'A'.toInt + 1 + 26
    }
  }

  override def run = for {
    file <- readTextFile("day03.txt")
    newLine <- getNewLine()

    rucksacks = for {
      line <- file.split(newLine).toList
    } yield line

    compartments = for {
      rucksack <- rucksacks
      first = rucksack.take(rucksack.length() / 2)
      second = rucksack.takeRight(rucksack.length() / 2)
    } yield (first, second)

    firstPriorities = for {
      (compartment1, compartment2) <- compartments

      appearance1 = appearanceSet(compartment1)
      appearance2 = appearanceSet(compartment2)

      intersect = appearance1 & appearance2

      priority = convert(intersect.head)
    } yield priority

    _ <- Console.printLine(
      s"The sum of the first priorities is ${firstPriorities.sum}!"
    )

    secondPriorities = for {
      elfGroup <- rucksacks.grouped(3).toList

      appearances = for {
        rucksack <- elfGroup
        appearance = appearanceSet(rucksack)
      } yield appearance

      universal = appearances.foldLeft(Set.empty[Char])((acc, elem) =>
        acc.union(elem)
      )

      badge = appearances.foldLeft(universal)((acc, elem) =>
        acc.intersect(elem)
      )

      priority = convert(badge.head)

    } yield priority

    _ <- Console.printLine(
      s"The sum of the second priorities is ${secondPriorities.sum}!"
    )
  } yield ()
}
