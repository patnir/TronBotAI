class direction:
    def __init__(self, x, y, width):
        self.x = x
        self.y = y
        self.width = width

    def moveRight(self, board):
        if self.x + 1 >= self.width or board[self.y][self.x + 1] != 0:
            print("cannot move right!")
            return False
        self.x += 1
        return True

    def moveLeft(self, board):
        if self.x - 1 <= 0 or board[self.y][self.x - 1] != 0:
            print("cannot move right!")
            return False
        self.x -= 1
        return True

    def moveDown(self, board):
        if self.y + 1 >= self.width or board[self.y + 1][self.x] != 0:
            print("cannot move down!")
            return False
        self.y += 1
        return True

    def moveUp(self, board):
        if self.y - 1 <= 0 or board[self.y - 1][self.x] != 0:
            print("cannot move up!")
            return False
        self.y -= 1
        return True
