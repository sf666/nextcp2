#!/bin/bash
#
# Installs the external forks nextCP/2 depends on into the local Maven repo.
# These are custom builds that are NOT published to Maven Central, so both a
# local dev checkout and the CI runner must build them once before nextCP/2:
#
#   * ik666/jupnp   -> org.jupnp:*:3.0.5.ik  (UPnP stack fork, see jupnp.version)
#   * sf666/musicbrainz
#
set -euo pipefail

rm -rf deps
mkdir deps
cd deps

# --- jUPnP fork (org.jupnp 3.0.5.ik) -------------------------------------
# Build only the two bundles nextCP/2 resolves; -am also builds their upstream
# reactor projects (parent POM + BOMs), so the integration tests (itests, slow /
# network-bound) and the tools module are skipped entirely.
git clone --depth 1 https://github.com/ik666/jupnp.git
cd jupnp
mvn -B -DskipTests -pl bundles/org.jupnp,bundles/org.jupnp.support -am install
cd ..

# --- MusicBrainz fork ----------------------------------------------------
git clone --depth 1 https://github.com/sf666/musicbrainz.git
cd musicbrainz
mvn -B install
cd ..

cd ..
