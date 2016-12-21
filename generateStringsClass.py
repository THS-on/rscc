#!/usr/bin/python

# Converts a ResourceBundle .properties file to only contain identifiers that
# comply to Java standards, and generates a .java file which loads the correct
# ResourceBundle at runtime and stored each string in a public field.


from datetime import datetime
from hashlib import sha256
from os import listdir
from re import match, split
from sys import argv

VERSION = "0.1.4"

THIS_NAME = "generateStringsClass.py"
CLASS_NAME = "Strings"
BUNDLE_DIR = "src/ch/imedias/rsccfx/localisation"
BUNDLE_NAME = BUNDLE_DIR + '/' + argv[1]
PACKAGE_NAME = "ch.imedias.rsccfx.localisation"
SOURCE_LOCATION = BUNDLE_DIR + '/' + CLASS_NAME + ".java"

# Strip non-word characters and underlines out of a string and format it to camelCase standards
def camelCaseify(string):
	res = split("[\\W_]+", string)
	res = ''.join(map(lambda s: s[0].upper() + s[1:], res))
	return res[0].lower() + res[1:]

# Split a string at an equals sign and return a tuple of head and tail
# where head is everything up to but not including the first occurrence of =
# camelCase is also enforced on the head
def splitAtEquals(string):
	if string:
		i = string.index('=')
		return (camelCaseify(string[:i]), string[i:])

def main():
	# Open the properties file in read mode and store contents as list of lines
	with open(BUNDLE_NAME + ".properties", 'r') as file:
		properties = file.readlines()
	
	# Store a hash of the contents as retrieved
	inHash = sha256(''.join(properties)).hexdigest()
	
	# Apply splitAtEquals to every element of properties
	properties = list(map(splitAtEquals, properties))
	
	# Put the file contents back together as a string
	outString = ''.join(map(lambda p: p[0] + p[1], properties))
	
	# Generate a hash from the edited string
	outHash = sha256(outString).hexdigest()
	
	# If changes occurred, open the file again, this time in write mode, and save new contents
	if inHash != outHash:
		with open(BUNDLE_NAME + ".properties", 'w') as file:
			file.write(outString)
		# Also apply to any other .properties files
		for bundle in listdir(BUNDLE_DIR):
			if match("Bundle\\w+\\.properties", bundle) != None:
				with open(BUNDLE_DIR + '/' + bundle, 'r') as file:
					contents = file.readlines()
				contents = ''.join(map(lambda p: p[0] + p[1], list(map(splitAtEquals, contents))))
				with open(BUNDLE_DIR + '/' + bundle, 'w') as file:
					file.write(contents)
				del (contents)

	
	# Try to retrieve hash from current version of generated code
	try:
		with open(SOURCE_LOCATION, 'r') as file:
			for i, line in enumerate(file):
				if i == 6:
					sourceHash = line[4:-1]
		sourceMissing = False
	except IOError:
		print ("[INFO] Source file not found, generating from scratch.")
		sourceMissing = True


	# If source exists and hash has not changed, we're done
	if sourceMissing or sourceHash != outHash:
		# Set up static boilerplate code for header and constructor
		header = "package %s;\n\n/**\n *  Code generated at %s\n *  by %s version %s\n" % \
				 (PACKAGE_NAME, datetime.today().strftime("%H:%M:%S %d.%m.%Y"), THIS_NAME, VERSION)
		header += " *  based on properties file with hash:\n *  %s\n */\n\n" % (outHash)
		header += "import java.util.Locale;\nimport java.util.ResourceBundle;\n\n"
		header += "public class %s {\n\n" % (CLASS_NAME)
		header += "  private ResourceBundle rscBndl;\n\n"
		ctor = "\n  public %s() {\n\n" % (CLASS_NAME)
		ctor += "    rscBndl = ResourceBundle.getBundle(\"%s\", Locale.getDefault());\n\n" % (BUNDLE_NAME)

		# Allocate and set a String field for each line in properties
		for identifier, _ in properties:
			header += "  public String %s;\n" % (identifier)
			ctor += "    {0} = rscBndl.getString(\"{0}\");\n".format(identifier)

		# Stitch it all together
		source = header + ctor + "  }\n}\n"

		# Write source code to disk
		with open(SOURCE_LOCATION, 'w') as file:
			file.write(source)
	elif not sourceMissing:
		print ("[INFO] Source file identical, nothing changed.")

# Do all the things
main()
