# Filter Apache HTTPd log entries based on HTTP code and method
#
# You can supply three input variables to the script:
# * http_method in [GET, POST, OPTIONS, HEAD, PUT, DELETE] (default: POST)
# * http_code in range 200..599 (default: 500)
# * verbose if set to any value turns on verbose logging (default: unset)
#
# Examples:
#
#     $ awk -v http_code=500 \
#         -v http_method=POST \
#         -v verbose=1 \
#         -f this_script.awk INPUT_FILE
#
#     $ awk -v http_code=200 \
#         -v http_method=POST \
#         -f this_script.awk INPUT_FILE
#

BEGIN {
  # setup the special variable if they should deviate from the defaults
  #FS = ",";   # Field Separator
  RS = "\n";  # Record Separator (lines)
  OFS = " ";  # Output Field Separator
  ORS = "\n"; # Output Record Separator (lines)

  # set defaults here if input variables are not set
  if (http_code == "") { http_code = "500"; }
  if (http_method == "") { http_method = "POST"; }
  if (counter == "") { counter = 0; }
  if (verbose != "") {
    print "Using values:";
    print " * http_code =", http_code;
    print " * http_method =", http_method;
    print " * counter =", counter;
    print " * ARGC =", ARGC;
  }
  print "=== HTTP Logs ==="
}
END {
  # Output summary information
  print "=== Matched:", counter, "===";
}

# your functions underneath right here
function displayLogEntry(logentry) {
  print ">>>", logentry;
}

# your expressions below
$6 == http_method && $9 == http_code {
  displayLogEntry($0);
  counter = counter + 1;
}
