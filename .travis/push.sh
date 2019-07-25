#!/bin/sh

# Commit the CHANGELOG modifications and new versioning
git commit -am 'Automatic release increment and changelog generation. Travis build $TRAVIS_BUILD_NUMBER pushed [skip ci]'

# Push the modifications into develop
git push origin HEAD:master

echo -e "Done\n"