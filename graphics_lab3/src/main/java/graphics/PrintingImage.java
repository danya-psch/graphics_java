package graphics;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PrintingImage extends Application{

	private HeaderBitmapImage image; 
	private int numberOfPixels;

	Color prpl = Color.rgb(136, 60, 133);
	Color green = Color.rgb(141, 193, 64);
	Color dark_green = Color.rgb(90, 137, 58);
	Color light_green = Color.rgb(216, 224, 65);
	Color wall1 = Color.rgb(227, 205, 147);
	Color wall2 = Color.rgb(249, 244, 232);
	Color roof1 = Color.rgb(146, 81, 161);
	Color roof2 = Color.rgb(195, 145, 194);
	Color dark_wood = Color.rgb(143, 88, 38);
	Color light_wood = Color.rgb(216, 166, 92);
	Color yellow = Color.rgb(247, 228, 24);
	Color orange = Color.rgb(249, 163, 22);
	Color blue = Color.rgb(202, 228, 248);
	
	public PrintingImage()
	{}
	
	public PrintingImage(HeaderBitmapImage image)
	{
		this.image = image;
	}
		
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		ReadingImageFromFile.loadBitmapImage("src/main/resources/trajectory.bmp");
		this.image = ReadingImageFromFile.pr.image;
		int width = (int)this.image.getWidth();
		int height = (int)this.image.getHeight();
		int half = (int)image.getHalfOfWidth();
		Group root = new Group();
		Scene scene = new Scene (root, width, height);
		Circle cir;
		int let = 0;
		int let1 = 0;
		int let2 = 0;
		char[][] map = new char[width][height];

		BufferedInputStream reader = new BufferedInputStream (new FileInputStream("pixels.txt"));

		for(int i=0;i<height;i++)  // поки не кінець зображення по висоті
		{
			for(int j=0;j<half;j++)  // поки не кінець зображення по довжині
			{
				let = reader.read();  // зчитуємо один символ з файлу
				let1 = let;
				let2 = let;
				let1 = let1&(0xf0);  // старший байт - перший піксель
				let1 = let1>>4;  // зсув на 4 розряди
				let2 = let2&(0x0f);  // молодший байт - другий піксель
				if(j*2<width) // так як 1 символ кодує 2 пікселі нам необхідно пройти до середини ширини зображення
				{
					cir = new Circle ((j)*2,(height-1-i),1,Color.valueOf((returnPixelColor(let1)))); // за допомогою стандартного
					// примітива Коло радіусом в 1 піксель та кольором визначеним за допомогою методу returnPixelColor малюємо піксель
					//root.getChildren().add(cir); //додаємо об'єкт в сцену
					if (returnPixelColor(let1) == "BLACK") // якщо колір пікселя чорний, то ставимо в масиві 1
					{
						map[j*2][height-1-i] = '1';
						numberOfPixels++; // збільшуємо кількість чорних пікселів
					}
					else
					{
						map[j*2][height-1-i] = '0';
					}
				}

				if(j*2+1<width) // для другого пікселя
				{
					cir = new Circle ((j)*2+1,(height-1-i),1,Color.valueOf((returnPixelColor(let2))));
					//root.getChildren().add(cir);
					if (returnPixelColor(let2) == "BLACK")
					{
						map[j*2+1][height-1-i] = '1';
						numberOfPixels++;
					}
					else
					{
						map[j*2+1][height-1-i] = '0';
					}
				}
			}
		}
		primaryStage.setScene(scene); // ініціалізуємо сцену
		primaryStage.show(); // візуалізуємо сцену
		reader.close();

		int[][] black;
		black = new int[numberOfPixels][2];
		int lich = 0;

		BufferedOutputStream writer = new BufferedOutputStream (new FileOutputStream("map.txt")); // записуємо карту для руху по траекторії в файл
		for(int i=0;i<height;i++)     // поки не кінець зображення по висоті
		{
			for(int j=0;j<width;j++)         // поки не кінець зображення по довжині
			{
				if (map[j][i] == '1')
				{
					black[lich][0] = j;
					black[lich][1] = i;
					lich++;
				}
				writer.write(map[j][i]);
			}
			writer.write(10);
		}
		writer.close();

		System.out.println("number of black color pixels = " + numberOfPixels);

		Path path2 = new Path();
		for (int l=0; l<numberOfPixels-1; l++)
		{
			path2.getElements().addAll(
					new MoveTo(black[l][0],black[l][1]),
					new LineTo(black[l+1][0],black[l+1][1])
			);
		}
		//////////////////////////////////////////////////////////////////////////////////////////

		Ellipse el1 = new Ellipse(200, 160, 80, 40);
		el1.setFill(green);
		root.getChildren().add(el1);

		Path path10 = new Path(
				new MoveTo(153, 140),
				new ArcTo(100, 30, 0, 153, 160, false, false ),
				new ArcTo(100, 20, 0, 200, 173, false, false ),
				new ArcTo(100, 30, 0, 242, 155, false, false ),
				new ArcTo(100, 30, 0, 242, 145, false, false )
		);
		path10.fillProperty().set(dark_green);
		path10.strokeWidthProperty().set(0);
		root.getChildren().add(path10);

		Path path1 = new Path(
				new MoveTo(145, 100),

				new ArcTo(14, 100, 0, 155, 155, false, false ),
				new ArcTo(100, 20, 0, 200, 170, false, false ),
				new LineTo(205, 110)
		);
		path1.fillProperty().set(wall1);
		path1.strokeWidthProperty().set(0);
		root.getChildren().add(path1);

		Path path3 = new Path(
				new MoveTo(205, 110),
				new LineTo(200, 170),
				new LineTo(240, 153),
				new LineTo(245, 98)
		);
		path3.fillProperty().set(wall2);
		path3.strokeWidthProperty().set(0);
		root.getChildren().add(path3);

		Path path4 = new Path(
				new MoveTo(132, 95),
				new QuadCurveTo( 170, 120, 205, 115),
				new QuadCurveTo( 215, 115, 255, 95),
				new QuadCurveTo( 265, 90, 215, 48),
				new QuadCurveTo( 210, 44, 190, 40),
				new LineTo(160, 50),
				new LineTo(132, 95)
		);
		path4.fillProperty().set(roof1);
		path4.strokeWidthProperty().set(0);
		root.getChildren().add(path4);

		Path path5 = new Path(
				new MoveTo(135, 95),
				new QuadCurveTo( 170, 117, 203, 113),
				new LineTo(190, 60),
				new LineTo(162, 52)
		);
		path5.fillProperty().set(roof2);
		path5.strokeWidthProperty().set(0);
		root.getChildren().add(path5);

		Path path6 = new Path(
				new MoveTo(163, 51),
				new LineTo(190, 58),
				new LineTo(213, 48),
				new LineTo(190,42)
		);
		path6.fillProperty().set(roof2);
		path6.strokeWidthProperty().set(0);
		root.getChildren().add(path6);

		Path path7 = new Path(
				new MoveTo(212, 165),
				new QuadCurveTo( 207, 121, 223, 117),
				new QuadCurveTo( 239, 119, 233, 157)
		);
		path7.fillProperty().set(dark_wood);
		path7.strokeWidthProperty().set(0);
		root.getChildren().add(path7);

		Path path8 = new Path(
				new MoveTo(216, 164),
				new QuadCurveTo( 211, 125, 223, 122),
				new QuadCurveTo( 235, 123, 230, 159)
		);
		path8.fillProperty().set(light_wood);
		path8.strokeWidthProperty().set(0);
		root.getChildren().add(path8);


		Circle crcl1 = new Circle(227, 140, 3);
		crcl1.setFill(yellow);
		root.getChildren().add(crcl1);

		Path path9 = new Path(
				new MoveTo(163, 122),
				new QuadCurveTo( 163, 140, 166, 144),
				new QuadCurveTo( 188, 150, 193, 148),
				new QuadCurveTo( 196, 127, 195, 127),
				new QuadCurveTo( 175, 120, 163, 122)
		);
		path9.fillProperty().set(dark_wood);
		path9.strokeWidthProperty().set(0);
		root.getChildren().add(path9);

		Path window1 = new Path(
				new MoveTo(165, 124),
				new LineTo(166,132),
				new LineTo(178,134),
				new LineTo(178,126)
		);
		window1.fillProperty().set(blue);
		window1.strokeWidthProperty().set(0);
		root.getChildren().add(window1);

		Path window2 = new Path(
				new MoveTo(166, 134),
				new LineTo(167,142),
				new LineTo(178,145),
				new LineTo(178,136)
		);
		window2.fillProperty().set(blue);
		window2.strokeWidthProperty().set(0);
		root.getChildren().add(window2);

		Path window3 = new Path(
				new MoveTo(180, 127),
				new LineTo(180,135),
				new LineTo(192,137),
				new LineTo(193,129)
		);
		window3.fillProperty().set(blue);
		window3.strokeWidthProperty().set(0);
		root.getChildren().add(window3);

		Path window4 = new Path(
				new MoveTo(180, 137),
				new LineTo(180,145),
				new LineTo(191,146),
				new LineTo(192,139)
		);
		window4.fillProperty().set(blue);
		window4.strokeWidthProperty().set(0);
		root.getChildren().add(window4);


		Path road2 = new Path(
				new MoveTo(191, 198),
				new QuadCurveTo( 190, 192, 210, 187),
				new QuadCurveTo( 236, 180, 229, 159)
		);
		road2.setStroke(dark_green);
		road2.setStrokeWidth(3);
		root.getChildren().add(road2);

		Path road = new Path(
				new MoveTo(170, 197),
				new QuadCurveTo( 175, 175, 210, 175),
				new QuadCurveTo( 225, 173, 220, 162),
				new LineTo(228,159),
				new QuadCurveTo( 235, 178, 210, 185),
				new QuadCurveTo( 190, 190, 190, 200)
		);
		road.fillProperty().set(yellow);
		road.strokeWidthProperty().set(0);
		root.getChildren().add(road);

		Path tree1 = new Path(
				new MoveTo(130, 155),
				new ArcTo( 10, 30, 0, 120, 125, false, false),
				new QuadCurveTo(127, 140, 126, 155)
		);
		tree1.fillProperty().set(dark_wood);
		tree1.strokeWidthProperty().set(0);
		root.getChildren().add(tree1);

		Path tree2 = new Path(
				new MoveTo(122, 130),
				new ArcTo( 20, 12, 0, 127, 110, false, false),
				new ArcTo( 10, 20, 0, 112, 110, false, false),
				new ArcTo( 10, 10, 0, 122, 130, false, false)
		);
		tree2.fillProperty().set(green);
		tree2.strokeWidthProperty().set(0);
		root.getChildren().add(tree2);

		Path pipe3 = new Path(
				new MoveTo(206, 80),
				new LineTo(222, 85),
				new LineTo(228, 80)
		);
		pipe3.setStroke(prpl);
		pipe3.setStrokeWidth(3);
		root.getChildren().add(pipe3);

		Path pipe1 = new Path(
				new MoveTo(205, 60),
				new LineTo(206, 80),
				new LineTo(222, 85),
				new LineTo(220, 63)
		);
		pipe1.fillProperty().set(orange);
		pipe1.strokeWidthProperty().set(0);
		root.getChildren().add( pipe1);

		Path pipe2 = new Path(
				new MoveTo(220, 63),
				new LineTo(222, 85),
				new LineTo(228, 80),
				new LineTo(226, 58)
		);
		pipe2.fillProperty().set(yellow);
		pipe2.strokeWidthProperty().set(0);
		root.getChildren().add( pipe2);

		Path pipe4 = new Path(
				new MoveTo(203, 60),
				new LineTo(220, 64),
				new LineTo(220, 56),
				new LineTo(203, 52)

		);
		pipe4.fillProperty().set(green);
		pipe4.strokeWidthProperty().set(0);
		root.getChildren().add( pipe4);

		Path pipe5 = new Path(
				new MoveTo(220, 64),
				new LineTo(220, 56),
				new LineTo(228, 51),
				new LineTo(228, 59)

		);
		pipe5.fillProperty().set(light_green);
		pipe5.strokeWidthProperty().set(0);
		root.getChildren().add( pipe5);


		Path pipe6 = new Path(
				new MoveTo(220, 56),
				new LineTo(228, 51),

				new LineTo(211, 47),
				new LineTo(203, 52)

		);
		pipe6.fillProperty().set(dark_green);
		pipe6.strokeWidthProperty().set(0);
		root.getChildren().add( pipe6);


//////////////////////////////////////////////////////////////////////////////////////////

		PathTransition pathTransition = new PathTransition();
		pathTransition.setDuration(Duration.millis(5000));
		pathTransition.setPath(path2);
		pathTransition.setNode(root);
		pathTransition.setAutoReverse(true);

		RotateTransition rotateTransition = new RotateTransition(Duration.millis(2000), root);
		rotateTransition.setByAngle(360f);
		rotateTransition.setCycleCount(3);

		ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(5000), root);
		scaleTransition.setToX(0.5f);
		scaleTransition.setToY(0.5f);
		scaleTransition.setAutoReverse(true);

		ParallelTransition parallelTransition = new ParallelTransition();
		parallelTransition.getChildren().addAll(
				rotateTransition,
				scaleTransition,
				pathTransition
		);
		parallelTransition.setCycleCount(Timeline.INDEFINITE);
		parallelTransition.setAutoReverse(true);
		parallelTransition.play();
	}
	
	private String returnPixelColor (int color) // ����� ��� ������������ ������� 16-������ ����������
	{
		String col = "BLACK";
		switch(color)
		   {
		      case 0: return "BLACK";     //BLACK;
		      case 1: return "LIGHTCORAL";  //LIGHTCORAL;
		      case 2: return "GREEN";     //GREEN
		      case 3: return "BROWN";     //BROWN
		      case 4: return "BLUE";      //BLUE;
		      case 5: return "MAGENTA";   //MAGENTA;
		      case 6: return "CYAN";      //CYAN;
		      case 7: return "LIGHTGRAY"; //LIGHTGRAY;
		      case 8: return "DARKGRAY";  //DARKGRAY;
		      case 9: return "RED";       //RED;
		      case 10:return "LIGHTGREEN";//LIGHTGREEN
		      case 11:return "YELLOW";    //YELLOW;
		      case 12:return "LIGHTBLUE"; //LIGHTBLUE;
		      case 13:return "LIGHTPINK";    //LIGHTMAGENTA
		      case 14:return "LIGHTCYAN";    //LIGHTCYAN;
		      case 15:return "WHITE";    //WHITE;
		   }
		   return col;
	}
		
	public static void main (String args[]) 
	{
	   launch(args);
	}

}
