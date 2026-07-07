#!/bin/bash
#
# Installs the external forks nextCP/2 depends on into the local Maven repo.
# These are custom builds that are NOT published to Maven Central, so both a
# local dev checkout and the CI runner must build them once before nextCP/2:
#
#   * ik666/jupnp       -> org.jupnp:*  (UPnP stack fork)
#   * sf666/musicbrainz -> de.sf666:musicbrainz
#
# Single source of truth for the versions is backend/pom.xml: this script reads
# the jupnp.version / musicbrainz.version properties from there and checks out
# the matching git tags. Bump the version in the pom and this script follows.
#   jupnp tag       == <jupnp.version>            (e.g. 3.0.5.ik)
#   musicbrainz tag == musicbrainz-<version>      (e.g. musicbrainz-1.4.2)
#
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
POM="$ROOT/backend/pom.xml"

read_prop() {
  # Extract a single-line Maven property value: read_prop <name>
  local val
  val="$(sed -n "s:.*<$1>\(.*\)</$1>.*:\1:p" "$POM" | head -1)"
  if [ -z "$val" ]; then
    echo "ERROR: property <$1> not found in $POM" >&2
    exit 1
  fi
  printf '%s' "$val"
}

JUPNP_VERSION="$(read_prop jupnp.version)"
MUSICBRAINZ_VERSION="$(read_prop musicbrainz.version)"
echo "==> Resolving forks: jupnp=$JUPNP_VERSION, musicbrainz=$MUSICBRAINZ_VERSION"

rm -rf deps
mkdir deps
cd deps

# --- jUPnP fork ----------------------------------------------------------
# Build only the two bundles nextCP/2 resolves; -am also builds their upstream
# reactor projects (parent POM + BOMs), so the integration tests (itests, slow /
# network-bound) and the tools module are skipped entirely.
git clone --depth 1 --branch "$JUPNP_VERSION" https://github.com/ik666/jupnp.git
cd jupnp
mvn -B -DskipTests -pl bundles/org.jupnp,bundles/org.jupnp.support -am install
cd ..

# --- MusicBrainz fork ----------------------------------------------------
git clone --depth 1 --branch "musicbrainz-$MUSICBRAINZ_VERSION" https://github.com/sf666/musicbrainz.git
cd musicbrainz
mvn -B install
cd ..

cd ..
