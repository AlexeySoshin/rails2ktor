require 'test_helper'

class CatsControllerTest < ActionDispatch::IntegrationTest
  test "should get create" do
    get cats_create_url
    assert_response :success
  end

end
