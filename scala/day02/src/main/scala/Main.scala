import zio._

import userUtil.UserUtil.{readTextFile, newLine}

object Main extends ZIOAppDefault {
  def strategy1(opponent: Shape, player: Shape): Int = {
    return player match {
      case Rock =>
        opponent match {
          case Rock     => 1 + 3
          case Paper    => 1 + 0
          case Scissors => 1 + 6
        }
      case Paper =>
        opponent match {
          case Rock     => 2 + 6
          case Paper    => 2 + 3
          case Scissors => 2 + 0
        }
      case Scissors =>
        opponent match {
          case Rock     => 3 + 0
          case Paper    => 3 + 6
          case Scissors => 3 + 3
        }
    }
  }

  def stategy2(opponent: Shape, result: Match): Int = {
    return opponent match {
      case Rock => {
        result match {
          case Win  => 6 + 2
          case Draw => 3 + 1
          case Lose => 0 + 3
        }
      }
      case Paper => {
        result match {
          case Win  => 6 + 3
          case Draw => 3 + 2
          case Lose => 0 + 1
        }
      }
      case Scissors => {
        result match {
          case Win  => 6 + 1
          case Draw => 3 + 3
          case Lose => 0 + 2
        }
      }
    }
  }

  def run = for {
    file <- readTextFile("day02.txt")

    rounds = for {
      line <- file.split(newLine).toList
      splited = line.split(' ').toList
      first = splited.head match {
        case "A" => Rock
        case "B" => Paper
        case "C" => Scissors
      }
      second = splited.last match {
        case "X" => Rock
        case "Y" => Paper
        case "Z" => Scissors
      }
      third = splited.last match {
        case "X" => Lose
        case "Y" => Draw
        case "Z" => Win
      }
      round = (first, second, third)
    } yield round

    first = for {
      round <- rounds
      value = strategy1(round._1, round._2)
    } yield value

    _ <- Console.printLine(
      s"If the first strategy goes well, my total score will be ${first.sum}!"
    )

    second = for {
      round <- rounds
      value = stategy2(round._1, round._3)
    } yield value

    _ <- Console.printLine(
      s"If the second strategy goes well, my total score will be ${second.sum}!"
    )

  } yield ()
}

sealed trait Shape
case object Rock extends Shape
case object Paper extends Shape
case object Scissors extends Shape

sealed trait Match
case object Win extends Match
case object Draw extends Match
case object Lose extends Match
