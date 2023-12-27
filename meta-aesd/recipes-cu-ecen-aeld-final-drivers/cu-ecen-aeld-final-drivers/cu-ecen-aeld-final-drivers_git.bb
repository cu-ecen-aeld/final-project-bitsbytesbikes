# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# Unable to find any files that looked like license statements. Check the accompanying
# documentation and source headers and set LICENSE and LIC_FILES_CHKSUM accordingly.
#
# NOTE: LICENSE is being set to "CLOSED" to allow you to at least start building - if
# this is not accurate with respect to the licensing of the software being built (it
# will not be in most cases) you must specify the correct value before using this
# recipe for anything other than initial testing/development!
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

SRC_URI = "git://git@github.com/bitsbytesbikes/cu-ecen-aeld-final-drivers.git;protocol=ssh;branch=main"

# Modify these as desired
PV = "1.0+git"
SRCREV = "e7f4d17ae3484ace0af928b475e0433d1581fb82"

S = "${WORKDIR}/git"

inherit module

FILES:${PN} += "${sysconfdir}"
FILES:${PN} += "/aesd/read.py"

inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "start-aesd-service"

do_install:append() {
    install -d ${D}/aesd
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${S}/read.py ${D}/aesd/read.py
    install -m 0755 ${S}/start-aesd-service ${D}${sysconfdir}/init.d
}
