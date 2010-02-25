package com.yammer.metrics.tests


import org.scalatest.matchers.MustMatchers
import org.scalatest.Spec
import com.yammer.metrics.Timer
import com.yammer.time.Duration

class TimerTest extends Spec with MustMatchers {
  val precision = 5.0 // milliseconds

  describe("timing an event") {
    it("returns the event's value") {
      val timer = new Timer
      timer.time { 1 + 1 } must equal(2)
    }

    it("records the duration of the event") {
      val timer = new Timer
      timer.time { Thread.sleep(10) }
      timer.mean.ms.toDouble must be(10.0 plusOrMinus precision)
    }

    it("records the existence of the event") {
      val timer = new Timer
      timer.time { Thread.sleep(10) }

      timer.count must be(1)
    }
  }

  describe("timing a series of events") {
    val timer = new Timer
    timer ++= List(
      Duration.inMillis(10),
      Duration.inMillis(20),
      Duration.inMillis(20),
      Duration.inMillis(30),
      Duration.inMillis(40)
    )

    it("calculates the maximum duration") {
      timer.max.ms.toDouble must be(40.0 plusOrMinus precision)
    }

    it("calculates the minimum duration") {
      timer.min.ms.toDouble must be(10.0 plusOrMinus precision)
    }

    it("calculates the mean") {
      timer.mean.ms.toDouble must be(24.0 plusOrMinus precision)
    }

    it("calculates the standard deviation") {
      timer.standardDeviation.ms.toDouble must be(11.4 plusOrMinus precision)
    }

    it("calculates the 99.9th percentile") {
      timer.p999.ms.toDouble must be(40.0 plusOrMinus precision)
    }

    it("records the count") {
      timer.count must be (5)
    }
  }
}
