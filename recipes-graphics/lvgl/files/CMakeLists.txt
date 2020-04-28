cmake_minimum_required(VERSION 3.10)
project(lvgl)
set(CMAKE_C_STANDARD 11)#C11
set(CMAKE_CXX_STANDARD 17)#C17
set(CMAKE_CXX_STANDARD_REQUIRED ON)

INCLUDE_DIRECTORIES(${PROJECT_SOURCE_DIR})

# Compile lvgl
file(GLOB_RECURSE LIBLVGL "lvgl/*.c")
add_library(lvgl STATIC ${LIBLVGL})

# Compile lv_drivers
file(GLOB_RECURSE LIBDRIVERS "lv_drivers/*.c")
add_library(drivers STATIC ${LIBDRIVERS})

# Install libraries
install(TARGETS lvgl drivers
        RUNTIME DESTINATION bin
        LIBRARY DESTINATION lib
		ARCHIVE DESTINATION lib
)

# Install headers
install(DIRECTORY "lvgl" DESTINATION include
	FILES_MATCHING PATTERN "*.h"
)

install(DIRECTORY "lv_drivers" DESTINATION include
	FILES_MATCHING PATTERN "*.h"
)

# if _conf.h are needed use this one
# install(DIRECTORY ${CMAKE_SOURCE_DIR}/ DESTINATION include
# 	FILES_MATCHING PATTERN "*.h"
# )