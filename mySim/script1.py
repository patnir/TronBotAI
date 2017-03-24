import sys
import random

from direction import *

WIDTH = 10


def initializeBoard(gBoard, gStatus):
    [gBoard.append(['.'] * WIDTH) for _ in range(WIDTH)]
    [gStatus.append([0] * WIDTH) for _ in range(WIDTH)]
    return gBoard


def printBoard(gBoard):
    for i in range(len(gBoard)):
        for j in range(len(gBoard)):
            sys.stdout.write(str(gBoard[i][j]) + " "),
        sys.stdout.write("\n")
    print("\n")


def movePlayer1(gBoard, gStatus):
    totalMoves = 10
    p1 = direction(0, 0, WIDTH)
    p2 = direction(WIDTH - 1, WIDTH - 1, WIDTH)
    gStatus[p1.y][p1.x] = 1
    gStatus[p2.y][p2.x] = 2
    printBoard(gStatus)
    for i in range(totalMoves):
        print(i)
        res = False
        while not res:
            dire = random.randint(0, 3)
            if dire == 0:
                res = p1.moveDown(gStatus)
            elif dire == 1:
                res = p1.moveLeft(gStatus)
            elif dire == 2:
                res = p1.moveRight(gStatus)
            else:
                res = p1.moveUp(gStatus)
        gStatus[p1.y][p1.x] = 1
        res = False
        while not res:
            dire = random.randint(0, 3)
            if dire == 0:
                res = p2.moveDown(gStatus)
            elif dire == 1:
                res = p2.moveLeft(gStatus)
            elif dire == 2:
                res = p2.moveRight(gStatus)
            else:
                res = p2.moveUp(gStatus)
        gStatus[p2.y][p2.x] = 2
        printBoard(gStatus)
    printBoard(gStatus)


def main():
    player1 = "+"
    gBoard = []
    gStatus = []

    initializeBoard(gBoard, gStatus)
    printBoard(gBoard)
    printBoard(gStatus)


    movePlayer1(gBoard, gStatus)

    return

if __name__ == '__main__':
    main()