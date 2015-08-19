Revision
CircleTriangleFan: A solution to the exercise asking to draw a circle using triangle fans

2D Transformation Examples
House: A simple example of writing a JOGL program to draw a house and play around with 2D transformations
RectScaleRotate: An example showing what can happen when you use non-uniform scaling followed by rotation.
RotateNotAtOrigin: Rotating around an arbitrary point using a combination of transformations

Examples using push and pop
Houses: Using different transformations to draw different houses, using push and pop to restore transformations between some of the successive houses
Sun: Draws different instances of suns (made of hierarchical structures of circles and lines) using different transformations
personDemo.View: Displays and allows the user to interactively animate a person. The person is implemented hierarchically using transformations

Examples moving the camera
RectScaleRotateCamera: Simple example, updating the camera view as well as transforming the model in the world.
personDemo.ViewWithCamera: Displays and allows the user to interactively animate a person and a camera. The display on the left shows the scene through the eyes of the camera - this is usually what we want to do. The display on the right shows the world and simply draws a box showing where the camera is in terms of the world.