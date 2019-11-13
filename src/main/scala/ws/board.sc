def onePlayerBoard(offset: Int): Array[Array[Int]] = {
  val array = Array.ofDim[Int](7, 8)
  for (i <- 0 to 6; j <- 0 to 7) {
    array(i)(j) = -1
  }

  array(0)(7) = 0 + offset
  array(1)(6) = 1 + offset

  array(2)(5) = 2 + offset
  array(3)(5) = 3 + offset
  array(4)(5) = 4 + offset
  array(5)(5) = 5 + offset
  array(6)(5) = 6 + offset

  array(6)(4) = 7 + offset
  array(6)(3) = 8 + offset
  array(6)(2) = 9 + offset
  array(6)(1) = 10 + offset

  array(5)(1) = 11 + offset
  array(4)(1) = 12 + offset
  array(3)(1) = 13 + offset
  array(2)(1) = 14 + offset
  array(1)(0) = 15 + offset
  array
}
val board1 = onePlayerBoard(0)
val board2 = onePlayerBoard(16)
val board3 = onePlayerBoard(32)
val board4 = onePlayerBoard(48)

for (y <- 6 to 0 by -1) {
  print(" . " * 7)
  for (x <- 7 to 0 by -1) {
    if (board1(y)(x) != -1) print("[" + "*" + "]")
    else print("   ")
  }
  print(" . " * 6+"\n")
}

for (x <- 0 to 7) {
  print(" . " * 1)
  for (y <- 6 to 0 by -1) {
    if (board2(y)(x) != -1) print("[" + "*" + "]")
    else print("   ")
  }
  print(" . " * 6+"\n")
}


