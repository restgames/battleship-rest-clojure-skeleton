# Clojure Battleship REST Service Example using Luminus and Liberator

This is a Clojure Battleship REST Service Example using Luminus and Liberator. This Battleship Engine is **really simple**. It always places the ships in the same position and shots randomly. With such an Artificial Intelligence (AI) you will not win any game. It's about you to improve it so you can win battles against your mates.

### What's REST Games?

Welcome to REST Games! Our goal is to provide you some coding challenges that go beyond the katas. You will implement a small JSON REST API that will play a well known game. The cool part comes when two mates develop the same JSON REST API and a _Referee_ can make them play one against the other. Cool, isn't it?

## Installation

To run the application you will need to install leiningen before. In order to do so you can checkout **[leiningen's installation instructions](http://leiningen.org/#install)** on their site. Once you have installed leiningen, just run

    echo "{:dev true, :port 3000, :nrepl-port 7000}" > .lein-env

On the application root in order to complete the installation. In order to run the application you will also need a redis instance on your localhost, so make sure redis it's installed on the system.

## Start REST Service

    redis-server &
    lein run