cmake_minimum_required(VERSION 3.5)
project(DataGenerator)

set(CMAKE_CXX_STANDARD 17)

add_executable(DataGenerator main.cpp
        NameData.h
        SpecializedForTimeline.h)